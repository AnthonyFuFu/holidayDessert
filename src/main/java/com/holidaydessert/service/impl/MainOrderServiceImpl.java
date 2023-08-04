package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.MainOrderDao;
import com.holidaydessert.model.MainOrder;
import com.holidaydessert.service.MainOrderService;

@Service
public class MainOrderServiceImpl implements MainOrderService {

	@Autowired
	private MainOrderDao mainOrderDao;

	@Override
	public List<Map<String, Object>> list(MainOrder mainOrder) {
		return mainOrderDao.list(mainOrder);
	}

	@Override
	public int getCount(MainOrder mainOrder) {
		return mainOrderDao.getCount(mainOrder);
	}

	@Override
	public void update(MainOrder mainOrder) {
		mainOrderDao.update(mainOrder);
	}

	@Override
	public void add(MainOrder mainOrder) {
		mainOrderDao.add(mainOrder);
	}

}
