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

	@Override
	public int getCount(CompanyInformation companyInformation) {
		return companyInformationDao.getCount(companyInformation);
	}

	@Override
	public void add(CompanyInformation companyInformation) {
		companyInformationDao.add(companyInformation);
	}

	@Override
	public void update(CompanyInformation companyInformation) {
		companyInformationDao.update(companyInformation);
	}

	@Override
	public void delete(CompanyInformation companyInformation) {
		companyInformationDao.delete(companyInformation);
	}

	@Override
	public List<Map<String, Object>> frontList(CompanyInformation companyInformation) {
		return companyInformationDao.frontList(companyInformation);
	}

}
