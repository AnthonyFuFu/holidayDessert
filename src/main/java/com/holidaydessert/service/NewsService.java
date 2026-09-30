package com.holidaydessert.service;

import java.util.LinkedHashMap;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsService {

	@Autowired
	private NewsDao newsDao;
	
	@Autowired
	private NewsRepository newsRepository;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getStringParam(dataTableParams, "search[value]", "").trim();
		String draw = PageableUtil.getStringParam(dataTableParams, "draw", "0");
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.NEWS_TABLE,
				SortFieldsConstant.DEFAULT,
				Sort.Direction.DESC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> newsPage = newsRepository.backList(keyword, pageable);
		// 4. 取得查詢結果（轉成可修改的 Map，避免 Tuple 包裝的唯讀 Map）
		List<Map<String, Object>> newsList = newsPage.getContent().stream()
				.map(row -> (Map<String, Object>) new LinkedHashMap<>(row))
				.toList();
		// 5. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(newsPage, newsList, draw);
	}

	public void add(News news) {
		newsDao.add(news);
	}

	public void update(News news) {
		newsDao.update(news);
	}

	public void delete(News news) {
		newsRepository.offlineById(news.getNewsId());
	}

	public News getData(News news) {
		// 查無資料時回傳空的 News（Controller 會直接取用欄位）
		return newsRepository.findDataById(news.getNewsId()).orElseGet(News::new);
	}

	public List<Map<String, Object>> getList() {
		return newsRepository.findOnlineList();
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
