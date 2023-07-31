package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.News;

public interface NewsDao {

	public List<Map<String, Object>> list(News news);

}
