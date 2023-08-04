package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import com.holidaydessert.model.ReceiptInformation;

public interface ReceiptInformationService {

	public List<Map<String, Object>> list(ReceiptInformation receiptInformation);

}
