package com.holidaydessert.dao;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ReceiptInformation;

public interface ReceiptInformationDao {

	public List<Map<String, Object>> list(ReceiptInformation receiptInformation);

}
