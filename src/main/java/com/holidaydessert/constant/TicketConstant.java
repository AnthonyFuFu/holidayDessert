package com.holidaydessert.constant;

import org.springframework.data.redis.core.script.DefaultRedisScript;

public class TicketConstant {

    // ─── Redis Key 規範 ───────────────────────────────────────────
    /** 票種靜態資訊 Hash：ticket:type:info:{typeId} */
    public static final String TICKET_TYPE_INFO_KEY   = "ticket:type:info:%d";
    /** 剩餘票數 String：ticket:remaining:{typeId} */
    public static final String TICKET_REMAINING_KEY   = "ticket:remaining:%d";
    /** 使用者已購數量 String：ticket:user:count:{typeId}:{memId} */
    public static final String TICKET_USER_COUNT_KEY  = "ticket:user:count:%d:%d";
    /** 初始化分布式鎖：ticket:init:lock:{ticketName} */
    public static final String INIT_LOCK_KEY          = "ticket:init:lock:%s";
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
    
    // ─── Lua Script：原子扣票 + 個人上限檢查 ──────────────────────
    // KEYS[1] = ticket:remaining:{typeId}
    // KEYS[2] = ticket:user:count:{typeId}:{memId}
    // ARGV[1] = 購買張數（字串）
    // ARGV[2] = 每人購買上限（字串）
	// ARGV[3] = userCount Key TTL（秒）
    // 返回：>= 0 成功（剩餘票數）| -1 票不夠 | -2 超過個人上限
    public static final DefaultRedisScript<Long> PURCHASE_SCRIPT;
    static {
        PURCHASE_SCRIPT = new DefaultRedisScript<>();
        PURCHASE_SCRIPT.setScriptText(
        	    "local remaining = redis.call('GET', KEYS[1]) " +
        	    "if not remaining or tonumber(remaining) < tonumber(ARGV[1]) then " +
        	    "    return -1 " +
        	    "end " +
        	    "local rawUserCount = redis.call('GET', KEYS[2]) " +
        	    "local userCount = rawUserCount and tonumber(rawUserCount) or 0 " +
        	    "if userCount + tonumber(ARGV[1]) > tonumber(ARGV[2]) then " +
        	    "    return -2 " +
        	    "end " +
        	    "redis.call('INCRBY', KEYS[2], ARGV[1]) " +
        	    "if not rawUserCount then " +
        	    "    redis.call('EXPIRE', KEYS[2], ARGV[3]) " +
        	    "end " +
        	    "return redis.call('DECRBY', KEYS[1], ARGV[1])"
        	);
        PURCHASE_SCRIPT.setResultType(Long.class);
	}
    
}
