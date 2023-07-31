package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.News;

public interface NewsService {

	public List<Map<String, Object>> list(News news);

}
