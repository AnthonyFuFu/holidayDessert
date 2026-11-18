package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.CompanyInformation;

@Repository
public class CompanyInformationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
	public void add(CompanyInformation companyInformation) {
		
		String sql = " INSERT INTO holiday_dessert.company_information "
				   + " (COM_NAME, COM_ADDRESS, COM_PHONE, COM_MEMO) "
				   + " VALUES(?, ?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] { companyInformation.getComName(), companyInformation.getComAddress(), companyInformation.getComPhone(), companyInformation.getComMemo() });
		
	}
	
	public void update(CompanyInformation companyInformation) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.company_information "
				   + " SET COM_NAME = ? , COM_ADDRESS = ?, COM_PHONE = ?, COM_MEMO = ? "
				   + " WHERE COM_ID = ? ";
		
		args.add(companyInformation.getComName());
		args.add(companyInformation.getComAddress());
		args.add(companyInformation.getComPhone());
		args.add(companyInformation.getComMemo());
		args.add(companyInformation.getComId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}
	
	public void delete(CompanyInformation companyInformation) {
		
		String sql = " DELETE FROM holiday_dessert.company_information "
				   + " WHERE COM_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { companyInformation.getComId() });
		
	}
	
}
