package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ReceiptInformationDao;
import com.holidaydessert.model.ReceiptInformation;
import com.holidaydessert.service.ReceiptInformationService;

@Service
public class ReceiptInformationServiceImpl implements ReceiptInformationService {

	@Autowired
	private ReceiptInformationDao receiptInformationDao;

	@Override
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation) {
		return receiptInformationDao.list(receiptInformation);
	}

	@Override
	public int getCount(ReceiptInformation receiptInformation) {
		return receiptInformationDao.getCount(receiptInformation);
	}

	@Override
	public List<Map<String, Object>> frontList(ReceiptInformation receiptInformation) {
		return receiptInformationDao.frontList(receiptInformation);
	}

	@Override
	public void add(ReceiptInformation receiptInformation) {
		receiptInformationDao.add(receiptInformation);
	}

	@Override
	public void edit(ReceiptInformation receiptInformation) {
		receiptInformationDao.edit(receiptInformation);
	}

}
