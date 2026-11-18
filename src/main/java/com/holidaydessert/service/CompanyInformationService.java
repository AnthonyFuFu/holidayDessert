package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.CompanyInformationDao;
import com.holidaydessert.model.CompanyInformation;
import com.holidaydessert.repository.CompanyInformationRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class CompanyInformationService {

	@Autowired
	private CompanyInformationDao companyInformationDao;
	
	@Autowired
	private CompanyInformationRepository companyInformationRepository;

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
				SortFieldsConstant.COMPANY_TABLE,
				SortFieldsConstant.COMPANY_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> companyPage = companyInformationRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(companyPage, PageableUtil.toMutableList(companyPage), draw);
	}

	public void add(CompanyInformation companyInformation) {
		companyInformationDao.add(companyInformation);
	}

	public void update(CompanyInformation companyInformation) {
		companyInformationDao.update(companyInformation);
	}

	public void delete(CompanyInformation companyInformation) {
		companyInformationDao.delete(companyInformation);
	}

	public CompanyInformation getData(CompanyInformation companyInformation) {
		// 查無資料時回傳空的 CompanyInformation（Controller 會直接取用欄位）
		return companyInformationRepository.findDataById(companyInformation.getComId()).orElseGet(CompanyInformation::new);
	}

	// =============================================
	// front
	// =============================================
	public List<Map<String, Object>> frontList() {
	    List<Map<String, Object>> list = companyInformationRepository.frontList();
	    return list.isEmpty() ? null : list;
	}
	
}
