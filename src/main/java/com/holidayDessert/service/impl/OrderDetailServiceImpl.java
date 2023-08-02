package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.OrderDetailDao;
import com.holidayDessert.model.OrderDetail;
import com.holidayDessert.service.OrderDetailService;

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
	public void update(OrderDetail orderDetail) {
		orderDetailDao.update(orderDetail);
	}

	@Override
	public void delete(OrderDetail orderDetail) {
		orderDetailDao.delete(orderDetail);
	}

	@Override
	public List<Map<String, Object>> frontList(OrderDetail orderDetail) {
		return orderDetailDao.frontList(orderDetail);
	}

	@Override
	public void add(OrderDetail orderDetail) {
		orderDetailDao.add(orderDetail);
	}

}
