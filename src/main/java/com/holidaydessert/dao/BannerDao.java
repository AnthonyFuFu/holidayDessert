package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.Banner;

public interface BannerDao {
	
	// back
	public List<Map<String, Object>> list(Banner banner);
	public void add(Banner banner);
	public void update(Banner banner);
	public void delete(Banner banner);
	public Banner getData(Banner banner);

	// front
	public List<Map<String, Object>> frontRandList(Banner banner);

}
