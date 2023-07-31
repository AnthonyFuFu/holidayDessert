package com.holidayDessert.dao;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.CompanyInformation;

public interface CompanyInformationDao {

	public List<Map<String, Object>> list(CompanyInformation companyInformation);

}
