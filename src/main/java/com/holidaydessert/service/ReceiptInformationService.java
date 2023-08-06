package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ReceiptInformation;

public interface ReceiptInformationService {

	// back
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation);
	public int getCount(ReceiptInformation receiptInformation);
	
	// front
	public List<Map<String, Object>> frontList(ReceiptInformation receiptInformation);
	public void add(ReceiptInformation receiptInformation);
	public void edit(ReceiptInformation receiptInformation);
	
}
