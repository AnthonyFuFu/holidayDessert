package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.OrderDetailDao;
import com.holidaydessert.model.OrderDetail;
import com.holidaydessert.repository.OrderDetailRepository;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(OrderDetail orderDetail) {
		return orderDetailDao.list(orderDetail);
	}
	public int getCount(OrderDetail orderDetail) {
		return orderDetailDao.getCount(orderDetail);
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
