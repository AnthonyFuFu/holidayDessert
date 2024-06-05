package com.holidaydessert.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.message.AuthException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.holidaydessert.service.TokenService;
import com.holidaydessert.utils.JWTUtil;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin
@Controller
@RequestMapping("/token")
@Api(tags = { "api拿token" })
public class TokenController {

	// 時效30分鐘
	private static long ACCESS_TOKEN_EXPIRATION = 1800000;
	// 時效1天
	private static long REFRESH_TOKEN_EXPIRATION = 86400000;
	// token issuer
	private static String issuer = "holidaydesserAPIKey";
	// token subject
	private static String subject = "holidaydessertAPI";

	@Autowired
	private TokenService tokenService;

	@PostMapping("/getToken")
	@ApiOperation(value = "獲取token", notes = "token時限30分鐘")
	public ResponseEntity<?> getToken(
			@ApiParam(name = "memEmail", value = "MEM_EMAIL", required = true) @RequestParam(value = "memEmail", required = true) String memEmail,
			@ApiParam(name = "memPassword", value = "MEM_PASSWORD", required = true) @RequestParam(value = "memPassword", required = true) String memPassword) {
		Map<String, Object> returnMap = new HashMap<>();
		try {
			List<Map<String, Object>> tokenList = new ArrayList<>();
			tokenList = tokenService.getToken(memEmail, memPassword);

			String access = "";
			String newRefresh = "";
			if (tokenList != null && tokenList.size() > 0) {
				access = JWTUtil.createJWT(subject, issuer, ACCESS_TOKEN_EXPIRATION);
				newRefresh = JWTUtil.createJWT(subject, issuer, REFRESH_TOKEN_EXPIRATION);

				returnMap.put("access", access);
				returnMap.put("refresh", newRefresh);
			} else {
				throw new AuthException();
			}
		} catch (AuthException e) {
			returnMap.put("status", "400");
			returnMap.put("message", "獲取TOKEN失敗");
			return new ResponseEntity<>(returnMap, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Map<String, Object>>(returnMap, HttpStatus.OK);
	}

	@PostMapping("/refresh")
	@ApiOperation(value = "刷新token", notes = "401:refresh token過期，請使用者重新登入\n 403:驗證JWT失敗 200:有效期限內+取得新access\n 參數 不用加上 Bearer  ")
	public ResponseEntity<?> refresh(@RequestParam String refresh) {
		Map<String, Object> returnMap = new HashMap<>();
		try {
			// 驗證JWT
			JWTUtil.validate(refresh);

			Claims claims = JWTUtil.decodeJWT(refresh);

			Date date = new Date();
			String now_time = String.valueOf(date.getTime()).substring(0, 10);

			Map<String, Object> result = new HashMap<String, Object>();
			// refresh token過期(請使用者重新登入)
			if (Integer.parseInt(claims.get("exp").toString()) < Integer.parseInt(now_time)) {
				returnMap.put("status", "401");
				returnMap.put("message", "refresh token過期");
				return new ResponseEntity<>(returnMap, HttpStatus.UNAUTHORIZED);
				// refresh token沒過期(重新發refresh、access token)
			} else {
				String access = "";
				String newRefresh = "";
				// 用token issuer判斷角色
				String issuer = JWTUtil.getIssuer("Bearer " + refresh);
				if (issuer.equals("ElearningAPIKey")) {
					access = JWTUtil.createJWT(subject, issuer, ACCESS_TOKEN_EXPIRATION);
					newRefresh = JWTUtil.createJWT(subject, issuer, REFRESH_TOKEN_EXPIRATION);
					// userDetailService.loadUserByUsername(account, "admin");
				} else {
					// 無此角色
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}
				result.put("access", access);
				result.put("refresh", newRefresh);
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
			}
		} catch (AuthException e) {
			e.printStackTrace();
			returnMap.put("status", "403");
			returnMap.put("message", "token驗證失敗");
			return new ResponseEntity<>(returnMap, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
