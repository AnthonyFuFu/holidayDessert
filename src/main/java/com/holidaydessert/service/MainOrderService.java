package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.MainOrderDao;
import com.holidaydessert.model.MainOrder;
import com.holidaydessert.repository.MainOrderRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class MainOrderService {

	@Autowired
	private MainOrderDao mainOrderDao;
	
	@Autowired
	private MainOrderRepository mainOrderRepository;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.ORDER_TABLE,
				SortFieldsConstant.ORDER_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> orderPage = mainOrderRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(orderPage, PageableUtil.toMutableList(orderPage), draw);
	}

	public void update(MainOrder mainOrder) {
		mainOrderDao.update(mainOrder);
	}

	// =============================================
	// front
	// =============================================
	@Transactional
	public void add(MainOrder mainOrder) {
		mainOrderRepository.save(mainOrder);
	}

	public List<Map<String, Object>> getMemOrderList(Integer memId) {
	    List<Map<String, Object>> list = mainOrderRepository.getMemOrderList(memId);
	    return list.isEmpty() ? null : list;
	}

}
