package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.NewsDao;
import com.holidaydessert.model.News;
import com.holidaydessert.repository.NewsRepository;
import com.holidaydessert.service.NewsService;

@Service
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsDao newsDao;
	
	@Autowired
	private NewsRepository newsRepository;
	
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
	public News getData(News news) {
		return newsDao.getData(news);
	}

	@Override
	public List<Map<String, Object>> getList() {
		return newsDao.getList();
	}
	
	@Override
	public List<Map<String, Object>> getListForBanner() {
		return newsDao.getListForBanner();
	}

	// =============================================
	// frontList
	// =============================================
	@Override
	public List<Map<String, Object>> frontList(News news) {
	    List<Map<String, Object>> list;

	    if (news.getStart() != null && !"".equals(news.getStart())) {
	        // 有分頁：用 Pageable 處理 LIMIT start, length
	        // LIMIT start, length → PageRequest.of(page, size)
	        // start = 第幾筆開始（offset），length = 每頁幾筆（size）
	        int start  = Integer.parseInt(news.getStart());
	        int length = Integer.parseInt(news.getLength());
	        int page   = start / length; // 換算成第幾頁

	        Pageable pageable = PageRequest.of(page, length);
	        list = newsRepository.frontList(pageable);
	    } else {
	        // 無分頁：查全部
	        list = newsRepository.frontListAll();
	    }

	    return list.isEmpty() ? null : list;
	}

	// =============================================
	// frontRandList
	// =============================================
	@Override
	public List<Map<String, Object>> frontRandList(News news) {
	    List<Map<String, Object>> list = newsRepository.frontRandList();
	    return list.isEmpty() ? null : list;
	}

}
