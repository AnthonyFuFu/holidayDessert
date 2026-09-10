package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.NewsDao;
import com.holidaydessert.model.News;
import com.holidaydessert.repository.NewsRepository;

@Service
public class NewsService {

	@Autowired
	private NewsDao newsDao;
	
	@Autowired
	private NewsRepository newsRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(News news) {
		return newsDao.list(news);
	}

	public int getCount(News news) {
		return newsDao.getCount(news);
	}

	public void add(News news) {
		newsDao.add(news);
	}

	public void update(News news) {
		newsDao.update(news);
	}

	public void delete(News news) {
		newsDao.delete(news);
	}

	public News getData(News news) {
		return newsDao.getData(news);
	}

	public List<Map<String, Object>> getList() {
		return newsDao.getList();
	}
	
	public List<Map<String, Object>> getListForBanner() {
		return newsDao.getListForBanner();
	}

	// =============================================
	// front
	// =============================================
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
	
	public List<Map<String, Object>> frontRandList(News news) {
	    List<Map<String, Object>> list = newsRepository.frontRandList();
	    return list.isEmpty() ? null : list;
	}
		
}
