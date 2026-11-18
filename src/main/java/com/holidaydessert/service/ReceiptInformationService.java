package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.model.ReceiptInformation;
import com.holidaydessert.repository.ReceiptInformationRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class ReceiptInformationService {

	@Autowired
	private ReceiptInformationRepository receiptInformationRepository;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.RECEIPT_TABLE,
				SortFieldsConstant.RECEIPT_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> receiptInformationPage = receiptInformationRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(receiptInformationPage, PageableUtil.toMutableList(receiptInformationPage), draw);
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
