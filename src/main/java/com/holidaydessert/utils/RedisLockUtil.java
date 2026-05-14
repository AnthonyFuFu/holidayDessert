package com.holidaydessert.utils;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
public class RedisLockUtil {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

    /**
     * 加鎖（SET NX EX 原子操作）
     */
    public boolean lock(String key, String value, long expireSeconds) {
        return Boolean.TRUE.equals(
            redisTemplate.opsForValue()
                .setIfAbsent(key, value, expireSeconds, TimeUnit.SECONDS)
        );
    }
    
    /**
     * Lua 腳本保證「驗證 value → 刪除」原子性
     * 防止：鎖過期後，誤刪其他執行緒加的鎖
     */
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptText(
            "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('DEL', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end"
        );
        UNLOCK_SCRIPT.setResultType(Long.class);
    }
    
    /**
     * 解鎖方式（必須帶 value）
     */
    public void unlock(String key, String value) {
        redisTemplate.execute(UNLOCK_SCRIPT, Collections.singletonList(key), value);
    }

}
