package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.CompanyInformationDao;
import com.holidaydessert.model.CompanyInformation;
import com.holidaydessert.repository.CompanyInformationRepository;

@Service
public class CompanyInformationService {

	@Autowired
	private CompanyInformationDao companyInformationDao;
	
	@Autowired
	private CompanyInformationRepository companyInformationRepository;
	
	// back
	public List<Map<String, Object>> list(CompanyInformation companyInformation) {
		return companyInformationDao.list(companyInformation);
	}

	public int getCount(CompanyInformation companyInformation) {
		return companyInformationDao.getCount(companyInformation);
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
		return companyInformationDao.getData(companyInformation);
	}

	// front
	public List<Map<String, Object>> frontList() {
	    List<Map<String, Object>> list = companyInformationRepository.frontList();
	    return list.isEmpty() ? null : list;
	}
	
}
