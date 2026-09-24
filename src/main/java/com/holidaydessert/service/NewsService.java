package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.NewsDao;
import com.holidaydessert.model.News;
import com.holidaydessert.repository.NewsRepository;
import com.holidaydessert.utils.PageableUtil;

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
	public Map<String, Object> frontList(Map<String, Object> params) {
		// 1. 取得查詢參數
		String keyword = PageableUtil.getStringParam(params, "keyword", "").trim();
		// 2. 建立分頁與排序
		Pageable pageable = PageableUtil.buildPageable(
				params,
				SortFieldsConstant.SEARCH_NEWS,
				SortFieldsConstant.DEFAULT,
				Sort.Direction.DESC
		);
		// 3. 查詢資料
		Page<News> newsPage = keyword.isEmpty() ? newsRepository.frontList(pageable) : newsRepository.frontListByKeyword(keyword, pageable);
		// 4. 取得查詢結果
		List<News> newsList = newsPage.getContent();
		// 5. 統一封裝回傳
		return PageableUtil.buildResult(newsPage, newsList);
	}
	
	public List<Map<String, Object>> frontRandList(News news) {
	    List<Map<String, Object>> list = newsRepository.frontRandList();
	    return list.isEmpty() ? null : list;
	}
		
}
