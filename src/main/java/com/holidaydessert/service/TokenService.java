package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

public interface TokenService {

	/***
	 * 核對資料
	 * @param String web_code
	 * @param String code_description
	 * @return
	 */
	public List<Map<String, Object>> getToken(String memEmail,String memPassword);
}
