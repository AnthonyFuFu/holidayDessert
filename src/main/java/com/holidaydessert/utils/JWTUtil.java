package com.holidaydessert.utils;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.message.AuthException;
import javax.xml.bind.DatatypeConverter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTUtil {
	public static String createJWT(String subject,String issuer,long ttlMillis) {
		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("holidaydesserAPIKey");
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).setIssuer(issuer).signWith(signatureAlgorithm, signingKey);
		
		// if it has been specified, let's add the expiration
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		
		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}
	
	public static Claims decodeJWT(String jwt) {
		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary("holidaydesserAPIKey")).parseClaimsJws(jwt)
				.getBody();
		return claims;
	}
	
	/**
	 * 驗證JWT
	 * @throws AuthException 
	 */
	public static void validate(String token) throws AuthException {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary("holidaydesserAPIKey");
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		try {
			Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
		} catch (SignatureException  e) {
			throw new AuthException("Invalid JWT signature.");
		}catch (MalformedJwtException  e) {
			throw new AuthException("Invalid JWT token.");
		}catch (UnsupportedJwtException e) {
			throw new AuthException("Unsupported JWT token");
		}catch (IllegalArgumentException  e) {
			 throw new AuthException("JWT token compact of handler are invalid");
		}
	}
	
//	/**
//	 * 從token中取得account
//	 */
//	public static String getAccount(String header) {
//		String access = header.substring(7, header.length());
//		Claims claims = decodeJWT(access);
//		return claims.getSubject();
//	}
//	
//	/**
//	 * 從token中取得id
//	 */
//	public static int getId(String header) {
//		String access = header.substring(7, header.length());
//		Claims claims = decodeJWT(access);
//		return Integer.parseInt(claims.getId());
//	}
	
	/**
	 * 從token中取得issuer
	 */
	public static String getIssuer(String header) {
		String access = header.substring(7, header.length());
		Claims claims = decodeJWT(access);
		return claims.getIssuer();
	}
	
	/**
	 * 從token中取得subject
	 */
	public static String getSubject(String header) {
		String access = header.substring(7, header.length());
		Claims claims = decodeJWT(access);
		return claims.getSubject();
	}
	
//	================================================================================================

//	private static final String SIGN = "F!@#$%^&*ADSA";
//
//	/*
//	 * 生成token header.payload.sign
//	 */
//	public static String getToken(Map<String, String> map) {
//		Calendar instance = Calendar.getInstance();
//		instance.add(Calendar.SECOND, 600);// 默認10分鐘過期
//		//創建jwt builder
//		JWTCreator.Builder builder = JWT.create();
//		//payload
//		map.forEach((k, v) -> {
//			builder.withClaim(k, v);
//		});
//		String token = builder.withExpiresAt(instance.getTime())// 指定令牌過期時間
//				.sign(Algorithm.HMAC256(SIGN));// 簽名
//		return token;
//	}
//
//	/*
//	 * 驗證token 合法性+獲取token信息方法(二合一)
//	 */
//	public static DecodedJWT verify(String token) {
//		return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
//
//	}
//	
}
