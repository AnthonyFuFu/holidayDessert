package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.MainOrderDao;
import com.holidaydessert.model.MainOrder;
import com.holidaydessert.repository.MainOrderRepository;
import com.holidaydessert.service.MainOrderService;

@Service
public class MainOrderServiceImpl implements MainOrderService {

	@Autowired
	private MainOrderDao mainOrderDao;
	
	@Autowired
	private MainOrderRepository mainOrderRepository;
	
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
	@Transactional
	public void add(MainOrder mainOrder) {
		mainOrderRepository.save(mainOrder);
	}

	@Override
	public List<Map<String, Object>> getMemOrderList(Integer memId) {
	    List<Map<String, Object>> list = mainOrderRepository.getMemOrderList(memId);
	    return list.isEmpty() ? null : list;
	}

}
