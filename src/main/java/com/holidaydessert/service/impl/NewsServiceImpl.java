package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.NewsDao;
import com.holidaydessert.model.News;
import com.holidaydessert.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsDao newsDao;
	
	@Override
	public List<Map<String, Object>> list(News news) {
		return newsDao.list(news);
	}

	@Override
	public int getCount(News news) {
		return newsDao.getCount(news);
	}

	@Override
	public void add(News news) {
		newsDao.add(news);
	}

	@Override
	public void update(News news) {
		newsDao.update(news);
	}

	@Override
	public void delete(News news) {
		newsDao.delete(news);
	}

	@Override
	public List<Map<String, Object>> frontList(News news) {
		return newsDao.frontList(news);
	}

	@Override
	public List<Map<String, Object>> frontRandList(News news) {
		return newsDao.frontRandList(news);
	}

}
