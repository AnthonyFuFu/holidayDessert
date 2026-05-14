package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.ReceiptInformationDao;
import com.holidaydessert.model.ReceiptInformation;
import com.holidaydessert.repository.ReceiptInformationRepository;
import com.holidaydessert.service.ReceiptInformationService;

@Service
public class ReceiptInformationServiceImpl implements ReceiptInformationService {

	@Autowired
	private ReceiptInformationDao receiptInformationDao;
	
	@Autowired
	private ReceiptInformationRepository receiptInformationRepository;
	
	@Override
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation) {
		return receiptInformationDao.list(receiptInformation);
	}

	@Override
	public int getCount(ReceiptInformation receiptInformation) {
		return receiptInformationDao.getCount(receiptInformation);
	}

	// =============================================
	// frontList
	// =============================================
	@Override
	public List<Map<String, Object>> frontList(ReceiptInformation receiptInformation) {
	    List<Map<String, Object>> list = receiptInformationRepository.frontList(receiptInformation.getMemId());
	    return list.isEmpty() ? null : list;
	}

	// =============================================
	// add
	// =============================================
	@Override
	@Transactional
	public void add(ReceiptInformation receiptInformation) {
	    receiptInformationRepository.save(receiptInformation);
	}

	// =============================================
	// edit
	// =============================================
	@Override
	@Transactional
	public void edit(ReceiptInformation receiptInformation) {
	    receiptInformationRepository.edit(
	        receiptInformation.getRcpId(),
	        receiptInformation.getRcpName(),
	        receiptInformation.getRcpCvs(),
	        receiptInformation.getRcpAddress(),
	        receiptInformation.getRcpPhone()
	    );
	}
}
