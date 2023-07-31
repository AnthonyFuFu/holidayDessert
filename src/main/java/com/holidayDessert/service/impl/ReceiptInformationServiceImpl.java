package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.ReceiptInformationDao;
import com.holidayDessert.model.ReceiptInformation;

@Service
public class ReceiptInformationServiceImpl implements ReceiptInformationService {

	@Autowired
	private ReceiptInformationDao receiptInformationDao;

	@Override
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation) {
		return receiptInformationDao.list(receiptInformation);
	}

}
