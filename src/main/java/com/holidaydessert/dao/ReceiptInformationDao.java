package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ReceiptInformation;

public interface ReceiptInformationDao {

	// back
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation);
	public int getCount(ReceiptInformation receiptInformation);
	
}
