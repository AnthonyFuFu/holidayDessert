package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Banner;

public interface BannerService {
	
	// back
	public List<Map<String, Object>> list(Banner banner);
	public void add(Banner banner);
	public void delete(Banner banner);
	
	// front
	public List<Map<String, Object>> frontRandList(Banner banner);
	
}
