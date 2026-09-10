package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.ReceiptInformationDao;
import com.holidaydessert.model.ReceiptInformation;
import com.holidaydessert.repository.ReceiptInformationRepository;

@Service
public class ReceiptInformationService {

	@Autowired
	private ReceiptInformationDao receiptInformationDao;
	
	@Autowired
	private ReceiptInformationRepository receiptInformationRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation) {
		return receiptInformationDao.list(receiptInformation);
	}
	
	public int getCount(ReceiptInformation receiptInformation) {
		return receiptInformationDao.getCount(receiptInformation);
	}

	// =============================================
	// front
	// =============================================
	public List<Map<String, Object>> frontList(ReceiptInformation receiptInformation) {
	    List<Map<String, Object>> list = receiptInformationRepository.frontList(receiptInformation.getMemId());
	    return list.isEmpty() ? null : list;
	}
	
	@Transactional
	public void add(ReceiptInformation receiptInformation) {
	    receiptInformationRepository.save(receiptInformation);
	}
	
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
