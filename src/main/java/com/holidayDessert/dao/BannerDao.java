package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.Banner;

public interface BannerDao {
	
	// back
	public List<Map<String, Object>> list(Banner banner);
	public int getCount(Banner banner);
	public void add(Banner banner);
	public void update(Banner banner);
	public void delete(Banner banner);

	// front
	public List<Map<String, Object>> frontRandList(Banner banner);

}
