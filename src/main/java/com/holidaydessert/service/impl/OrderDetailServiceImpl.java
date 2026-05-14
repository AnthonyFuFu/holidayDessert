package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.OrderDetailDao;
import com.holidaydessert.model.OrderDetail;
import com.holidaydessert.repository.OrderDetailRepository;
import com.holidaydessert.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailDao orderDetailDao;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Override
	public List<Map<String, Object>> list(OrderDetail orderDetail) {
		return orderDetailDao.list(orderDetail);
	}

	@Override
	public int getCount(OrderDetail orderDetail) {
		return orderDetailDao.getCount(orderDetail);
	}

	// =============================================
	// frontOrderDetails
	// =============================================
	@Override
	public List<Map<String, Object>> frontOrderDetails(OrderDetail orderDetail) {
	    List<Map<String, Object>> list = orderDetailRepository.frontOrderDetails(orderDetail.getOrdId());
	    return list.isEmpty() ? null : list;
	}

	// =============================================
	// add
	// =============================================
	@Override
	@Transactional
	public void add(OrderDetail orderDetail) {
	    orderDetailRepository.save(orderDetail);
	}

}
