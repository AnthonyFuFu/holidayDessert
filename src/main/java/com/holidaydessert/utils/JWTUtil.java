package com.holidaydessert.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import jakarta.security.auth.message.AuthException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public final class JWTUtil {

	private JWTUtil() {
	}

	/*
	 * HS256 至少需要 32 bytes 的 key。
	 *
	 * 正式環境不要把 secret 寫死在程式裡， 建議改放 application.properties 或環境變數。
	 */
	private static final String SECRET = "holiday-dessert-jwt-secret-key-very-secure";

	private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	// 建立 JWT
	public static String createJWT(String subject, String issuer, long ttlMillis) {

		Date now = new Date();
		Date expiration = new Date(now.getTime() + ttlMillis);

		return Jwts.builder().subject(subject).issuer(issuer).issuedAt(now).expiration(expiration).signWith(SIGNING_KEY)
				.compact();
	}

	// 解析並驗證 JWT
	public static Claims decodeJWT(String jwt) {
		String token = removeBearerPrefix(jwt);
		return Jwts.parser().verifyWith(SIGNING_KEY).build().parseSignedClaims(token).getPayload();
	}

	// 驗證 JWT
	public static void validate(String token) throws AuthException {
		try {
			decodeJWT(token);
		} catch (ExpiredJwtException e) {
			throw new AuthException("JWT token expired.");
		} catch (JwtException e) {
			throw new AuthException("Invalid JWT token.");
		} catch (IllegalArgumentException e) {
			throw new AuthException("JWT token is invalid.");
		}
	}

	// 取得 issuer。可以傳入： - 純 token - Bearer token
	public static String getIssuer(String token) {
		return decodeJWT(token).getIssuer();
	}

	// 取得 subject
	public static String getSubject(String token) {
		return decodeJWT(token).getSubject();
	}

	// 移除 Bearer 前綴
	private static String removeBearerPrefix(String token) {
		if (token == null || token.isBlank()) {
			throw new IllegalArgumentException("JWT token is empty.");
		}
		if (token.startsWith("Bearer ")) {
			return token.substring(7);
		}
		return token;
	}
	
}
