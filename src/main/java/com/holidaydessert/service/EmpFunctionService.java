package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.dao.EmpFunctionDao;
import com.holidaydessert.model.EmpFunction;
import com.holidaydessert.repository.EmpFunctionRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class EmpFunctionService {

	@Autowired
	private EmpFunctionRepository empFunctionRepository;

	@Autowired
	private EmpFunctionDao empFunctionDao;

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
				SortFieldsConstant.EMP_FUNCTION_TABLE,
				SortFieldsConstant.EMP_FUNCTION_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> empFunctionPage = empFunctionRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(empFunctionPage, PageableUtil.toMutableList(empFunctionPage), draw);
	}

	public void add(EmpFunction empFunction) {
		empFunctionDao.add(empFunction);
	}

	public void update(EmpFunction empFunction) {
		empFunctionDao.update(empFunction);
	}

	public EmpFunction getData(EmpFunction empFunction) {
		return empFunctionDao.getData(empFunction);
	}
	
	public List<Map<String, Object>> getAdminListToAuth() {
		return empFunctionDao.getAdminListToAuth();
	}

}
