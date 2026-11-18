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
import com.holidaydessert.model.OrderDetail;
import com.holidaydessert.repository.OrderDetailRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;

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
				SortFieldsConstant.ORDER_DETAIL_TABLE,
				SortFieldsConstant.ORDER_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> orderDetailPage = orderDetailRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(orderDetailPage, PageableUtil.toMutableList(orderDetailPage), draw);
	}

	// =============================================
	// front
	// =============================================
	public List<Map<String, Object>> frontOrderDetails(OrderDetail orderDetail) {
	    List<Map<String, Object>> list = orderDetailRepository.frontOrderDetails(orderDetail.getOrdId());
	    return list.isEmpty() ? null : list;
	}
	
	@Transactional
	public void add(OrderDetail orderDetail) {
	    orderDetailRepository.save(orderDetail);
	}
	
}
