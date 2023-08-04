package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.OrderDetailDao;
import com.holidaydessert.model.OrderDetail;
import com.holidaydessert.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

	@Autowired
	private OrderDetailDao orderDetailDao;

	@Override
	public List<Map<String, Object>> list(OrderDetail orderDetail) {
		return orderDetailDao.list(orderDetail);
	}

	@Override
	public int getCount(OrderDetail orderDetail) {
		return orderDetailDao.getCount(orderDetail);
	}

	@Override
	public OrderDetail data(OrderDetail orderDetail) {
		return orderDetailDao.data(orderDetail);
	}

	@Override
	public void add(OrderDetail orderDetail) {
		orderDetailDao.add(orderDetail);
	}

}
