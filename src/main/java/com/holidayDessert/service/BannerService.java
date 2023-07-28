package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Banner;

public interface BannerService {
	
	public List<Map<String, Object>> list(Banner banner);

}
