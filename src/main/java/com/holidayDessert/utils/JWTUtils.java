package com.holidayDessert.utils;

import java.util.Calendar;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtils {
	private static final String SIGN = "F!@#$%^&*ADSA";

	/*
	 * 生成token header.payload.sing
	 */
	public static String getToken(Map<String, String> map) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.SECOND, 600);// 默認10分鐘過期
		//創建jwt builder
		JWTCreator.Builder builder = JWT.create();
		//payload
		map.forEach((k, v) -> {
			builder.withClaim(k, v);
		});
		String token = builder.withExpiresAt(instance.getTime())// 指定令牌過期時間
				.sign(Algorithm.HMAC256(SIGN));// 簽名
		return token;
	}

	/*
	 * 驗證token 合法性+獲取token信息方法(二合一)
	 */
	public static DecodedJWT verify(String token) {
		return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);

	}
}