package com.holidaydessert.constant;

import java.util.List;

public class AllowedOrigin {

	public static final String OFFICAIL_HOST = "http://localhost:8080";
	public static final String OFFICAIL_VUE_HOST = "http://localhost:8081";

	public static final List<String> OFFICAIL = List.of(
		OFFICAIL_HOST,
		OFFICAIL_VUE_HOST
	);

}
