package com.holidaydessert.constant;

import org.springframework.data.redis.core.script.DefaultRedisScript;

public class TicketConstant {

    // ─────────────────────────────────────────
    // ✅ Redis Key 設計（統一規範，兩把 Key 各司其職）
    // ─────────────────────────────────────────
	public static final String TICKET_INFO_KEY  = "ticket:%s:info";    // 票券完整物件（票名、價格、場次）
	public static final String TICKET_COUNT_KEY = "ticket:%s:count";   // 票數（專供 Lua 原子扣減）
    public static final String INIT_LOCK_KEY    = "ticket:%s:initLock"; // 初始化分布式鎖
    
    // ─────────────────────────────────────────
    // Redis Lua 原子扣票腳本（預編譯）
    // ─────────────────────────────────────────
    public static final DefaultRedisScript<Long> DECR_SCRIPT;
    static {
        DECR_SCRIPT = new DefaultRedisScript<>();
        DECR_SCRIPT.setScriptText(
            "local count = redis.call('GET', KEYS[1]) " +
            "if count and tonumber(count) > 0 then " +
            "    return redis.call('DECR', KEYS[1]) " +
            "else " +
            "    return -1 " +
            "end"
        );
        DECR_SCRIPT.setResultType(Long.class);
    }

}
