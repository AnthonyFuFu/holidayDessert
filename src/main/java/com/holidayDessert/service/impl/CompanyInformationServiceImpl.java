package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.CompanyInformationDao;
import com.holidayDessert.model.CompanyInformation;
import com.holidayDessert.service.CompanyInformationService;

@Service
public class CompanyInformationServiceImpl implements CompanyInformationService {

	@Autowired
	private CompanyInformationDao companyInformationDao;

	@Override
	public List<Map<String, Object>> list(CompanyInformation companyInformation) {
		return companyInformationDao.list(companyInformation);
	}

}
