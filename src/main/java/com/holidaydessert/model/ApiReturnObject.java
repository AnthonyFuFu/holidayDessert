package com.holidaydessert.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiReturnObject {
	
	private Integer code;
	private String message;
	private Object result;

    // =============================================
    // 2xx：成功
    // =============================================
    public static final int OK                    = 200; // 請求成功
    public static final int CREATED               = 201; // 建立成功
    public static final int ACCEPTED              = 202; // 已接受，處理中
    public static final int NO_CONTENT            = 204; // 成功但無回傳內容

    // =============================================
    // 3xx：重新導向
    // =============================================
    public static final int MOVED_PERMANENTLY     = 301; // 永久轉移
    public static final int FOUND                 = 302; // 暫時轉移
    public static final int NOT_MODIFIED          = 304; // 資源未變更

    // =============================================
    // 4xx：客戶端錯誤
    // =============================================
    public static final int BAD_REQUEST           = 400; // 請求格式錯誤
    public static final int UNAUTHORIZED          = 401; // 未登入 / 未驗證
    public static final int FORBIDDEN             = 403; // 無權限
    public static final int NOT_FOUND             = 404; // 資源不存在
    public static final int METHOD_NOT_ALLOWED    = 405; // 請求方法不允許
    public static final int REQUEST_TIMEOUT       = 408; // 請求逾時
    public static final int CONFLICT              = 409; // 資源衝突（如重複註冊）
    public static final int UNPROCESSABLE_ENTITY  = 422; // 格式正確但語意錯誤
    public static final int TOO_MANY_REQUESTS     = 429; // 請求過於頻繁

    // =============================================
    // 5xx：伺服器錯誤
    // =============================================
    public static final int INTERNAL_SERVER_ERROR = 500; // 伺服器內部錯誤
    public static final int NOT_IMPLEMENTED       = 501; // 功能未實作
    public static final int BAD_GATEWAY           = 502; // 閘道錯誤
    public static final int SERVICE_UNAVAILABLE   = 503; // 服務不可用
    public static final int GATEWAY_TIMEOUT       = 504; // 閘道逾時

    // =============================================
    // 靜態工廠方法：常用情境快速建立
    // =============================================
    public static ApiReturnObject success(String message, Object result) {
        return new ApiReturnObject(OK, message, result);
    }

    public static ApiReturnObject created(String message, Object result) {
        return new ApiReturnObject(CREATED, message, result);
    }

    public static ApiReturnObject noContent(String message) {
        return new ApiReturnObject(NO_CONTENT, message, null);
    }

    public static ApiReturnObject badRequest(String message) {
        return new ApiReturnObject(BAD_REQUEST, message, null);
    }

    public static ApiReturnObject unauthorized(String message) {
        return new ApiReturnObject(UNAUTHORIZED, message, null);
    }

    public static ApiReturnObject forbidden(String message) {
        return new ApiReturnObject(FORBIDDEN, message, null);
    }

    public static ApiReturnObject notFound(String message) {
        return new ApiReturnObject(NOT_FOUND, message, null);
    }

    public static ApiReturnObject conflict(String message) {
        return new ApiReturnObject(CONFLICT, message, null);
    }

    public static ApiReturnObject tooManyRequests(String message) {
        return new ApiReturnObject(TOO_MANY_REQUESTS, message, null);
    }

    public static ApiReturnObject serverError(String message) {
        return new ApiReturnObject(INTERNAL_SERVER_ERROR, message, null);
    }

    public static ApiReturnObject serviceUnavailable(String message) {
        return new ApiReturnObject(SERVICE_UNAVAILABLE, message, null);
    }
    
}
