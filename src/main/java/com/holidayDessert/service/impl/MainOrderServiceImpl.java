package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.MainOrderDao;
import com.holidayDessert.model.MainOrder;
import com.holidayDessert.service.MainOrderService;

@Service
public class MainOrderServiceImpl implements MainOrderService {

	@Autowired
	private MainOrderDao mainOrderDao;

	@Override
	public List<Map<String, Object>> list(MainOrder mainOrder) {
		return mainOrderDao.list(mainOrder);
	}

}
