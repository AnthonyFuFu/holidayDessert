package com.holidayDessert.service;

import java.util.List;
import java.util.Map;

import com.holidayDessert.model.CompanyInformation;

public interface CompanyInformationService {
	
	// back
	public List<Map<String, Object>> list(CompanyInformation companyInformation);
	public int getCount(CompanyInformation companyInformation);
	public void add(CompanyInformation companyInformation);
	public void update(CompanyInformation companyInformation);
	public void delete(CompanyInformation companyInformation);
	
	// front
	public List<Map<String, Object>> frontList(CompanyInformation companyInformation);
	
}
