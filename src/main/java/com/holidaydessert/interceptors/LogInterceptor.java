package com.holidaydessert.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.holidaydessert.model.Employee;

//@Component
public class LogInterceptor implements HandlerInterceptor {

//	@Autowired
//	private EditLogService editLogService;
	
//	private EditLog editLog = new EditLog();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Gson gson = new Gson();
		Employee employeeSession = (Employee) request.getSession().getAttribute("userAccountSession");
		
		if (employeeSession != null) {
//			editLog.setCreate_by(userAccountSession.getEmployee_no());	
		}else {
//			editLog.setCreate_by("0");
		}
	    
		String Args = gson.toJson(request.getParameterMap());
		System.out.println(Args);
//		editLog.setIp(request.getRemoteAddr());
//		editLog.setMethod(request.getMethod());
//		editLog.setUrl(request.getRequestURI());
//		editLog.setContent(Args);
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		if ("OPTIONS".equals(request.getMethod().toUpperCase())) {
//			return true;
//		}
////		// 檢查請求路徑是否以 "/holidayDessert/front" 開頭，如果是，則直接放行
////		if (request.getRequestURI().startsWith("/holidayDessert/front")) {
////			return true;
////		}
////		// 檢查請求路徑是否為 "/holidayDessert/index"，如果是，則直接放行
////		if ("/holidayDessert/index".equals(request.getRequestURI())) {
////			return true;
////		}
//		// 檢查請求路徑是否以 "/holidayDessert/admin" 開頭，如果是，則進行 JWT 驗證
//		if (request.getRequestURI().startsWith("/holidayDessert/adminx")) {
//			Map<String, Object> map = new HashMap<>();
//			System.out.println("preHandle");
//			try {
//				// 獲取請求頭中的令牌
//				String autBearertoken = request.getHeader(HttpHeaders.AUTHORIZATION);
//				System.out.println(autBearertoken);
//				// 驗證token是不是null,避免nullpointerException
//				if (autBearertoken != null) {
//					String trimtoken = autBearertoken.replace("Bearer", "");
//					String token = trimtoken.trim();
//					System.out.println(token);
//					// 驗證令牌
//					JWTUtil.verify(token);
//					// 驗證成功 放行請求
//					return true;
//				} else {
//					map.put("msg", "token is null");
//					response.setStatus(401);
//				}
//			} catch (SignatureVerificationException e) {
//				map.put("msg", "簽名不一致");
//				response.setStatus(411);
//				e.printStackTrace();
//			} catch (TokenExpiredException e) {
//				map.put("msg", "令牌過期");
//				response.setStatus(401);
//				e.printStackTrace();
//			} catch (AlgorithmMismatchException e) {
//				map.put("msg", "算法不匹配");
//				response.setStatus(401);
//				e.printStackTrace();
//			} catch (InvalidClaimException e) {
//				map.put("msg", "失效的payload");
//				response.setStatus(401);
//				e.printStackTrace();
//			} catch (Exception e) {
//				map.put("msg", "token無效");
//				response.setStatus(401);
//				e.printStackTrace();
//			}
//			// 設置狀態
//			map.put("state", false);
//			// 將map 轉為json jackson
//			String json = new ObjectMapper().writeValueAsString(map);
//			response.setContentType("application/json;charset=UTF-8");
//			response.getWriter().println(json);
//			return false;
//		}
//		// 放行其他路徑，不進行 JWT 驗證
//		return true;
//	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		int status = response.getStatus();
		System.out.println(status);
//		editLog.setStatus(Integer.toString(status));		
//		editLogService.addLog(editLog);
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}