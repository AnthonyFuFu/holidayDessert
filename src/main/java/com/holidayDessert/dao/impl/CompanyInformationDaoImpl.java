package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.CompanyInformationDao;
import com.holidayDessert.model.CompanyInformation;

@Repository
public class CompanyInformationDaoImpl implements CompanyInformationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(CompanyInformation companyInformation) {

		String sql = " SELECT * FROM holiday_dessert.company_information ";

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		list = jdbcTemplate.queryForList(sql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(CompanyInformation companyInformation) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void add(CompanyInformation companyInformation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(CompanyInformation companyInformation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(CompanyInformation companyInformation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> frontList(CompanyInformation companyInformation) {
		// TODO Auto-generated method stub
		return null;
	}

}
