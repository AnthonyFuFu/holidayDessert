package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Ticket;
import com.holidaydessert.service.TicketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/tickets", produces = "application/json; charset=UTF-8")
public class TicketController {
	
//	用戶                     後端                          DB/Redis
//	 │                         │                              │
//	 ├──登入─────────────────►│                              │
//	 │◄──JWT Token────────────┤                              │
//	 │                         │                              │
//	 ├──瀏覽活動列表──────────►│ 查 ticket                   │
//	 │                         ├─ WHERE status=1              │
//	 │                         │  AND NOW() BETWEEN           │
//	 │                         │  SALE_START AND SALE_END ───►│
//	 │◄──活動 + 票種列表───────┤◄────────────────────────────┤
//	 │                         │                              │
//	 ├──選票種 + 數量──────────►│                              │
//	 │                         ├─ 檢查剩餘票數 ───────────────►│
//	 │                         ├─ 檢查每人上限                │
//	 │                         ├─ 扣減庫存（原子操作）─────────►│
//	 │◄──建立訂單成功───────────┤◄─ 回傳訂單ID ────────────────┤
//	 │                         │                              │
//	 ├──付款（15分鐘內）────────►│                              │
//	 │                         ├─ 更新 STATUS=1 ──────────────►│
//	 │◄──購票完成───────────────┤                              │
//	 │                         │                              │
//	 │  [若逾時未付款]           │                              │
//	 │                         ├─ 定時任務掃描 EXPIRE 欄位     │
//	 │                         ├─ STATUS=5（逾時取消）─────────►│
//	 │                         └─ 還原庫存 ────────────────────►│


	
//	[後端啟動 / 售票前]
//		DB → 讀取 ticket_type JOIN ticket
//		→ 寫入 Redis 三層結構
//
//	[使用者搶票]
//		全程只碰 Redis（速度快）
//		└─ 成功扣減後 → 非同步寫入 DB 訂單
//
//	[逾時未付款]
//		定時任務掃描 DB
//		└─ 將 remaining 還回 Redis
//		└─ 更新 user:count
//
//	[售票結束]
//		Redis 資料可清除或保留作統計

	
//	【Step 1】售票前
//	POST /api/tickets/init?ticketName=五月天演唱會
//
//	DB ticket_type (4筆) → JOIN FETCH ticket
//	  ↓ typeId=1 (搖滾區)
//	    ticket:type:info:1  → Hash { typeName, typePrice:3200, typeMaxPerPerson:4, ... }
//	    ticket:remaining:1  → "100"
//	    LOCAL_REMAINING[1]  → AtomicInteger(100)
//	  ↓ typeId=2 (看台區)
//	    ticket:type:info:2  → Hash { ... }
//	    ticket:remaining:2  → "300"
//	    LOCAL_REMAINING[2]   AtomicInteger(300)
//	  ... 以此類推
//
//	─────────────────────────────────────────────────────
//	【Step 2】搶票（以 typeId=1，memId=101，quantity=2 為例）
//	POST /api/tickets/purchase?typeId=1&memId=101&quantity=2
//
//	Layer 1: Semaphore.tryAcquire()               → 通過（< 200）
//	Layer 2: LOCAL_REMAINING[1].get() = 100 >= 2  → 通過
//	         Redis HGET ticket:type:info:1 typeMaxPerPerson → "4"
//	Layer 3: Lua Script 執行
//	         GET ticket:remaining:1       → "100" ≥ 2 ✓
//	         GET ticket:user:count:1:101  → null → 0 + 2 ≤ 4 ✓
//	         INCRBY ticket:user:count:1:101 2  → "2"
//	         DECRBY ticket:remaining:1    2    → 98（返回值）
//	         result = 98
//	Layer 4: @Async persistOrder(1, 101, 2, 3200)
//	         ├── decrementRemaining(1, 2) → UPDATE ticket_type SET remaining=98
//	         └── ticketOrderRepository.save(order) → 新訂單，amount=6400，expire=+15min

    @Autowired
    private TicketService ticketService;
    
    // ─── 搶票核心 API ─────────────────────────────────────────────

    /**
     * 售票前初始化（不需傳 count，從 DB 的 TYPE_REMAINING 讀）
     * POST /api/tickets/init?ticketName=五月天演唱會
     */
    @PostMapping("/init")
    public ResponseEntity<ApiReturnObject> init(@RequestParam String ticketName) {
        try {
            String msg = ticketService.initTickets(ticketName);

            if (msg.contains("預熱完成"))   return ResponseEntity.ok(ApiReturnObject.success(msg, null));
            if (msg.contains("進行中"))     return ResponseEntity.ok(ApiReturnObject.conflict(msg));
            if (msg.contains("找不到活動")) return ResponseEntity.ok(ApiReturnObject.notFound(msg));

            return ResponseEntity.ok(ApiReturnObject.serverError(msg));

        } catch (Exception e) {
            log.error("[Controller] init 異常 ticketName={}", ticketName, e);
            return ResponseEntity.ok(ApiReturnObject.serverError("初始化失敗，請稍後再試"));
        }
    }

    /**
     * 搶票（以票種為單位）
     * POST /api/tickets/purchase?typeId=1&memId=101&quantity=2
     */
    @PostMapping("/purchase")
    public ResponseEntity<ApiReturnObject> purchase(
            @RequestParam Integer typeId,
            @RequestParam Integer memId,
            @RequestParam(defaultValue = "1") Integer quantity) {
        try {
            String msg = ticketService.purchaseTicket(typeId, memId, quantity);

            if (msg.contains("搶票成功"))       return ResponseEntity.ok(ApiReturnObject.success(msg, null));
            if (msg.contains("售罄"))           return ResponseEntity.ok(ApiReturnObject.conflict(msg));
            if (msg.contains("個人購買上限"))   return ResponseEntity.ok(ApiReturnObject.badRequest(msg));
            if (msg.contains("系統繁忙"))       return ResponseEntity.ok(ApiReturnObject.tooManyRequests(msg));

            return ResponseEntity.ok(ApiReturnObject.serverError(msg));

        } catch (Exception e) {
            log.error("[Controller] purchase 異常 typeId={} memId={}", typeId, memId, e);
            return ResponseEntity.ok(ApiReturnObject.serverError("系統錯誤，請稍後再試"));
        }
    }

    /**
     * 查詢票種剩餘票數
     * GET /api/tickets/remain?typeId=1
     */
    @GetMapping("/remain")
    public ResponseEntity<ApiReturnObject> remain(@RequestParam Integer typeId) {
        try {
            int count = ticketService.remainCount(typeId);
            return ResponseEntity.ok(ApiReturnObject.success("查詢成功 剩餘票數：", count));
        } catch (Exception e) {
            log.error("[Controller] remain 異常 typeId={}", typeId, e);
            return ResponseEntity.ok(ApiReturnObject.serverError("查詢失敗，請稍後再試"));
        }
    }
    // ─── 管理用 CRUD ─────────────────────────────────────────────

    /** POST /api/tickets/cache */
    @PostMapping("/cache")
    public ResponseEntity<ApiReturnObject> saveTicket(@RequestBody Ticket ticket) {
        try {
            ticketService.saveToRedis(ticket);
            return ResponseEntity.ok(ApiReturnObject.success("儲存成功", null));
        } catch (Exception e) {
            log.error("[Controller] saveTicket 異常 ticketId={}", ticket.getTicketId(), e);
            return ResponseEntity.ok(ApiReturnObject.serverError("儲存失敗，請稍後再試"));
        }
    }

    /** GET /api/tickets/cache/{id} */
    @GetMapping("/cache/{id}")
    public ResponseEntity<ApiReturnObject> getTicket(@PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getFromRedis(id);
            if (ticket != null) {
                return ResponseEntity.ok(ApiReturnObject.success("查詢成功", ticket));
            }
            return ResponseEntity.ok(ApiReturnObject.notFound("查無此票券 id=" + id));
        } catch (Exception e) {
            log.error("[Controller] getTicket 異常 id={}", id, e);
            return ResponseEntity.ok(ApiReturnObject.serverError("查詢失敗，請稍後再試"));
        }
    }

    /** DELETE /api/tickets/cache/{id} */
    @DeleteMapping("/cache/{id}")
    public ResponseEntity<ApiReturnObject> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteFromRedis(id);
            return ResponseEntity.ok(ApiReturnObject.success("刪除成功", null));
        } catch (Exception e) {
            log.error("[Controller] deleteTicket 異常 id={}", id, e);
            return ResponseEntity.ok(ApiReturnObject.serverError("刪除失敗，請稍後再試"));
        }
    }
    
}
