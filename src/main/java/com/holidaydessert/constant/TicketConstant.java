package com.holidaydessert.constant;

import java.util.List;

import org.springframework.data.redis.core.script.DefaultRedisScript;

public class TicketConstant {

    // ─── Redis Key 規範 ───────────────────────────────────────────
    /** 票種靜態資訊 Hash：ticket:type:info:{typeId} */
    public static final String TICKET_TYPE_INFO_KEY   = "ticket:type:info:%d";
    /** 剩餘票數 String：ticket:remaining:{typeId} */
    public static final String TICKET_REMAINING_KEY   = "ticket:remaining:%d";
    /** 使用者已購數量 String：ticket:user:count:{typeId}:{memId} */
    public static final String TICKET_USER_COUNT_KEY  = "ticket:user:count:%d:%d";
    /** 單一票種所有使用者已購數量（SCAN 用）：ticket:user:count:{typeId}:* */
    public static final String TICKET_USER_COUNT_PATTERN = "ticket:user:count:%d:*";
    /** 初始化分布式鎖：ticket:init:lock:{ticketName} */
    public static final String INIT_LOCK_KEY          = "ticket:init:lock:%s";
    /** 可售座位池 List（元素為「排-座」）：ticket:seats:{typeId} */
    public static final String TICKET_SEATS_KEY       = "ticket:seats:%d";
    /** 管理用 Ticket by ID：ticket:id:{ticketId} */
    public static final String TICKET_ID_KEY          = "ticket:id:%d";

    // ─── Hash 欄位名稱 ────────────────────────────────────────────
    public static final String FIELD_TYPE_NAME            = "typeName";
    public static final String FIELD_TYPE_PRICE           = "typePrice";
    public static final String FIELD_TYPE_MAX_PER_PERSON  = "typeMaxPerPerson";
    public static final String FIELD_TICKET_NAME          = "ticketName";
    public static final String FIELD_TICKET_STATUS        = "ticketStatus";
    public static final String FIELD_SALE_START           = "saleStart";
    public static final String FIELD_SALE_END             = "saleEnd";

    // ─── 業務常數 ─────────────────────────────────────────────────
    /** 付款期限（分鐘） */
    public static final int ORDER_EXPIRE_MINUTES = 15;
    /** Redis Key TTL（小時） */
    public static final long REDIS_TTL_HOURS = 24;
	// 常數宣告（秒）
    public static final long USER_COUNT_TTL_SECONDS = 24 * 3600L; // 與活動 TTL 一致
    
    // ─── Lua Script：原子扣票 + 個人上限檢查 + 電腦配位 ─────────────
    // KEYS[1] = ticket:remaining:{typeId}
    // KEYS[2] = ticket:user:count:{typeId}:{memId}
    // KEYS[3] = ticket:seats:{typeId}（自由座票種不存在此 Key，則不配位）
    // ARGV[1] = 購買張數（字串）
    // ARGV[2] = 每人購買上限（字串）
	// ARGV[3] = userCount Key TTL（秒）
    // 返回：{剩餘票數, 座位1, 座位2...} 成功 | {"-1"} 票不夠 | {"-2"} 超過個人上限
    // 座位池依「前排優先、同排連號」排列，LPOP 連續取出即為相鄰座位
    @SuppressWarnings("rawtypes")
    public static final DefaultRedisScript<List> PURCHASE_SCRIPT;
    static {
        PURCHASE_SCRIPT = new DefaultRedisScript<>();
        PURCHASE_SCRIPT.setScriptText(
        	    "local quantity = tonumber(ARGV[1]) " +
        	    "local remaining = redis.call('GET', KEYS[1]) " +
        	    "if not remaining or tonumber(remaining) < quantity then " +
        	    "    return {'-1'} " +
        	    "end " +
        	    "local rawUserCount = redis.call('GET', KEYS[2]) " +
        	    "local userCount = rawUserCount and tonumber(rawUserCount) or 0 " +
        	    "if userCount + quantity > tonumber(ARGV[2]) then " +
        	    "    return {'-2'} " +
        	    "end " +
        	    "redis.call('INCRBY', KEYS[2], quantity) " +
        	    "if not rawUserCount then " +
        	    "    redis.call('EXPIRE', KEYS[2], ARGV[3]) " +
        	    "end " +
        	    "local result = { tostring(redis.call('DECRBY', KEYS[1], quantity)) } " +
        	    "for i = 1, quantity do " +
        	    "    local seat = redis.call('LPOP', KEYS[3]) " +
        	    "    if not seat then break end " +
        	    "    table.insert(result, seat) " +
        	    "end " +
        	    "return result"
        	);
        PURCHASE_SCRIPT.setResultType(List.class);
	}
    
}
