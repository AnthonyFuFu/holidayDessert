package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

public interface TokenDao {

	/***
	 * 核對資料
	 * @param String web_code
	 * @param String code_description
	 * @return
	 */
	public List<Map<String, Object>> getToken(String code,String description);
}
