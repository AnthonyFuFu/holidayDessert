package com.holidaydessert.constant;

import java.util.List;

public class AllowedOrigin {

	public static final String OFFICAIL_HOST = "http://localhost:8080";
	public static final String OFFICAIL_VUE_HOST = "http://localhost:8081";

    // CorsConfig 使用（setAllowedOrigins 接受 List<String>）
	public static final List<String> OFFICAIL = List.of(
		OFFICAIL_HOST,
		OFFICAIL_VUE_HOST
	);
	
    // WebSocketConfig 使用（setAllowedOrigins 接受 String...）
    public static final String[] OFFICAIL_ARRAY = OFFICAIL.toArray(new String[0]);
    
}
