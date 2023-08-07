package com.holidaydessert.interceptors;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.utils.JWTUtils;

@Component
public class JWTInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
			return true;
		}

//		// 檢查請求路徑是否以 "/holidayDessert/front" 開頭，如果是，則直接放行
//		if (request.getRequestURI().startsWith("/holidayDessert/front")) {
//			return true;
//		}
//		// 檢查請求路徑是否為 "/holidayDessert/index"，如果是，則直接放行
//		if ("/holidayDessert/index".equals(request.getRequestURI())) {
//			return true;
//		}

		// 檢查請求路徑是否以 "/holidayDessert/admin" 開頭，如果是，則進行 JWT 驗證
		if (request.getRequestURI().startsWith("/holidayDessert/adminx")) {
			
			Map<String, Object> map = new HashMap<>();
			System.out.println("preHandle");
			try {
				// 獲取請求頭中的令牌
				String autBearertoken = request.getHeader(HttpHeaders.AUTHORIZATION);
				System.out.println(autBearertoken);
				// 驗證token是不是null,避免nullpointerException
				if (autBearertoken != null) {
					String trimtoken = autBearertoken.replace("Bearer", "");
					String token = trimtoken.trim();
					System.out.println(token);
					// 驗證令牌
					JWTUtils.verify(token);
					// 驗證成功 放行請求
					return true;
				} else {
					map.put("msg", "token is null");
					response.setStatus(401);
				}
			} catch (SignatureVerificationException e) {
				map.put("msg", "簽名不一致");
				response.setStatus(411);
				e.printStackTrace();
			} catch (TokenExpiredException e) {
				map.put("msg", "令牌過期");
				response.setStatus(401);
				e.printStackTrace();
			} catch (AlgorithmMismatchException e) {
				map.put("msg", "算法不匹配");
				response.setStatus(401);
				e.printStackTrace();
			} catch (InvalidClaimException e) {
				map.put("msg", "失效的payload");
				response.setStatus(401);
				e.printStackTrace();
			} catch (Exception e) {
				map.put("msg", "token無效");
				response.setStatus(401);
				e.printStackTrace();
			}
			// 設置狀態
			map.put("state", false);
			// 將map 轉為json jackson
			String json = new ObjectMapper().writeValueAsString(map);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().println(json);
			return false;
		}
		// 放行其他路徑，不進行 JWT 驗證
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("afterCompletion");
	}
}