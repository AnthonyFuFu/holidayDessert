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

}
