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
	
}