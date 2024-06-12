package com.holidaydessert.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockUtil {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public Boolean lock(String key, String value, long expireTime) {
		return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
	}

	public void unlock(String key) {
		redisTemplate.delete(key);
	}

}
