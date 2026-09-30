package com.holidaydessert.constant;

import java.util.Map;

public class SortFieldsConstant {
	
	// Search File Default
    public static final String DEFAULT = "NEWS_CREATE";
    public static final String PD_PIC_DEFAULT = "PD_PIC_ID";
	
	// 前後端排序對應關係
	public static final Map<String, String> SEARCH_NEWS = Map.of(
			// frontend field name, backend field name
            "newsId", "NEWS_ID",
            "newsName", "NEWS_NAME",
            "newsStart", "NEWS_START",
            "newsEnd", "NEWS_END",
            "newsCreate", "NEWS_CREATE"
    );
	
	public static final Map<String, String> SEARCH_NEWS_LIST = Map.ofEntries(
	        // frontend field name, backend entity field name
	        Map.entry("newsId", "NEWS_ID"),
	        Map.entry("newsName", "NEWS_NAME"),
	        Map.entry("newsStart", "NEWS_START"),
	        Map.entry("newsEnd", "NEWS_END"),
	        Map.entry("newsCreate", "NEWS_CREATE")
	);
	
	// 後台 DataTables 排序對應關係
	public static final Map<String, String> NEWS_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("NEWS_ID", "NEWS_ID"),
			Map.entry("NEWS_NAME", "NEWS_NAME"),
			Map.entry("NEWS_CONTENT", "NEWS_CONTENT"),
			Map.entry("PM_NAME", "PM_NAME"),
			Map.entry("STATUS", "NEWS_STATUS"),
			Map.entry("NEWS_START", "NEWS_START"),
			Map.entry("NEWS_END", "NEWS_END"),
			Map.entry("NEWS_CREATE", "NEWS_CREATE")
	);
	
}
