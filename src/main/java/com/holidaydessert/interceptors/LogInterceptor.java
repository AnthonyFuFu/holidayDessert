package com.holidaydessert.interceptors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.holidaydessert.model.EditLog;
import com.holidaydessert.model.Employee;
import com.holidaydessert.utils.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

//@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

//	@Autowired
//	private EditLogService editLogService;
	
    private static final String LOG_ATTRIBUTE = LogInterceptor.class.getName() + ".EDIT_LOG";
    private static final String LOGGED_ATTRIBUTE = LogInterceptor.class.getName() + ".LOGGED";

    private final ObjectMapper objectMapper = new ObjectMapper();
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 靜態資源不記錄
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
        // 避免同一個 Request 因 forward/error dispatch 被重複寫入
        if (request.getAttribute(LOGGED_ATTRIBUTE) != null) {
            return true;
        }
		HttpSession session = request.getSession(false);
		Employee employeeSession = null;
		if (session != null) {
			employeeSession = (Employee) session.getAttribute("employeeSession");
		}
        EditLog editLog = new EditLog();

        editLog.setLogIp(CommonUtil.getRemoteHost(request));
        editLog.setLogUrl(request.getRequestURI());
        editLog.setLogMethod(request.getMethod());
        editLog.setLogContent(getRequestParameters(request));

        if (employeeSession != null) {
            editLog.setLogCreateBy(employeeSession.getEmpName());
            editLog.setLogType("employee");
        } else {
            editLog.setLogCreateBy("0");
            editLog.setLogType("anonymous");
        }
        request.setAttribute(LOG_ATTRIBUTE, editLog);
//		log.info("method={}, uri={}, query={}, employeeSession={}, handler={}", request.getMethod(), request.getRequestURI(), request.getQueryString(), employeeSession, handler.getClass().getSimpleName());
		return true;
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
//			log.info("preHandle");
//			try {
//				// 獲取請求頭中的令牌
//				String autBearertoken = request.getHeader(HttpHeaders.AUTHORIZATION);
//				log.info(autBearertoken);
//				// 驗證token是不是null,避免nullpointerException
//				if (autBearertoken != null) {
//					String trimtoken = autBearertoken.replace("Bearer", "");
//					String token = trimtoken.trim();
//					log.info(token);
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
	private String getRequestParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterMap == null || parameterMap.isEmpty()) {
			return "{}";
		}
		Map<String, String[]> safeParameterMap = new LinkedHashMap<>(parameterMap);
		// 絕對不要把密碼寫進 Log
		safeParameterMap.remove("empPassword");
		safeParameterMap.remove("password");
		safeParameterMap.remove("confirmPassword");
		try {
			return objectMapper.writeValueAsString(safeParameterMap);
		} catch (JsonProcessingException e) {
			log.warn("Request parameter 轉 JSON 失敗", e);
			return "{}";
		}
	}
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		EditLog editLog = (EditLog) request.getAttribute(LOG_ATTRIBUTE);
		if (editLog == null) {
			return;
		}
		// 防止 afterCompletion 重複執行時重複寫入
		if (request.getAttribute(LOGGED_ATTRIBUTE) != null) {
			return;
		}
		request.setAttribute(LOGGED_ATTRIBUTE, Boolean.TRUE);
		editLog.setLogHttpStatusCode(String.valueOf(response.getStatus()));
		if (ex != null) {
			log.error("request error: method={}, uri={}, status={}", request.getMethod(), request.getRequestURI(), response.getStatus(), ex);
		} else {
//			log.info("request completed: method={}, uri={}, status={}", request.getMethod(), request.getRequestURI(), response.getStatus());
		}
		try {
//			editLogService.addLog(editLog);
		} catch (Exception saveException) {
			log.error("寫入 edit_log 失敗", saveException); // 不要因為寫 Log 失敗，影響原本的頁面回應
		}

	}
	
}