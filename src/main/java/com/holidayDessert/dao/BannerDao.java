package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Banner;

public interface BannerDao {
	
	public List<Map<String, Object>> list(Banner banner);
	
}
