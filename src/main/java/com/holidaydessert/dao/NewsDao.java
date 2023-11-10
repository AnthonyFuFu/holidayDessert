package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.News;

public interface NewsDao {

	// back
	public List<Map<String, Object>> list(News news);
	public int getCount(News news);
	public void add(News news);
	public void update(News news);
	public void delete(News news);
	public News getData(News news);
	public List<Map<String, Object>> getList();
	public List<Map<String, Object>> getListForBanner();
	
	// front
	public List<Map<String, Object>> frontList(News news);
	public List<Map<String, Object>> frontRandList(News news);
	
}
