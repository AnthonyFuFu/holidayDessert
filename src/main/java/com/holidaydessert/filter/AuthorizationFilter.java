package com.holidaydessert.filter;

import javax.security.auth.message.AuthException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.holidaydessert.utils.JWTUtil;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.Date;

@WebFilter(urlPatterns = "/*")
public class AuthorizationFilter implements Filter {

//	private static final String FIXTOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2ODg2Mjg2OTksImlzcyI6InRrYi1lbGVhcm5pbmdBUEktcHJvamVjdCJ9.a_r_vx9NMSYYUZTZqT0Yqcnf3ttOouY07CsddtZm9Cc";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// 检查请求的 URL
		String requestUrl = httpRequest.getRequestURL().toString();
		if (requestUrl.contains("swagger-ui.html") || requestUrl.contains("swagger") || requestUrl.contains("api-docs")
				|| requestUrl.contains("token") || requestUrl.contains("test")) {
			// 如果是 Swagger UI 的 URL，直接放行
			chain.doFilter(request, response);
			return;
		}
		// 检查授权信息
		String authorization = httpRequest.getHeader("Authorization");
		String bearer = "Bearer ";
		if (authorization != null && authorization.startsWith(bearer)) {
			String token = authorization.substring(bearer.length());
			try {
				// 驗證JWT
				JWTUtil.validate(token);
				// 驗證token時效
				Claims claims = JWTUtil.decodeJWT(token);
				Date date = new Date();
				String nowTime = String.valueOf(date.getTime()).substring(0, 10);
				if (Integer.parseInt(claims.get("exp").toString()) < Integer.parseInt(nowTime)) {
					// access token timeout
					httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token過期");
				} else {
					// 這邊是過惹
				}
			} catch (AuthException e) {
				e.printStackTrace();
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "token驗證失敗");
			}
			// 授权信息符合预期，继续处理请求
			chain.doFilter(request, response);
		} else {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "token驗證失敗");
		}
		// 其他 Filter 方法（init() 和 destroy()）的实现...
	}
}
