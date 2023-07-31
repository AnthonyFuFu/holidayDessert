package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.CompanyInformation;

public interface CompanyInformationService {

	public List<Map<String, Object>> list(CompanyInformation companyInformation);

}
