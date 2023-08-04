package com.holidaydessert.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Base {

	private Integer count;
	private Integer total_page;
	private Integer page_count;
	private Integer page;
	
	private String start;
	private String length;
	private String keyword;
	private List<Map<String, Object>> data;
	private Integer draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	
	private String searchText;
	
}
