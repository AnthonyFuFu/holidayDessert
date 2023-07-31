package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.ReceiptInformation;

public interface ReceiptInformationDao {

	public List<Map<String, Object>> list(ReceiptInformation receiptInformation);

}
