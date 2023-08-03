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
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM holiday_dessert.company_information ";
		
		if (companyInformation.getSearchText() != null && companyInformation.getSearchText().length() > 0) {
			String[] searchText = companyInformation.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(COM_NAME, ?) > 0"
					+  " OR INSTR(COM_ADDRESS, ?) > 0 "
					+  " OR INSTR(COM_PHONE, ?) > 0 "
					+  " OR INSTR(COM_MEMO, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if (companyInformation.getStart() != null && !"".equals(companyInformation.getStart())) {
			sql += " LIMIT " + companyInformation.getStart() + "," + companyInformation.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(CompanyInformation companyInformation) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.company_information ";
		
		if (companyInformation.getSearchText() != null && companyInformation.getSearchText().length() > 0) {
			String[] searchText = companyInformation.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(COM_NAME, ?) > 0"
					+  " OR INSTR(COM_ADDRESS, ?) > 0 "
					+  " OR INSTR(COM_PHONE, ?) > 0 "
					+  " OR INSTR(COM_MEMO, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(CompanyInformation companyInformation) {
		
		String sql = " INSERT INTO holiday_dessert.company_information "
				   + " (COM_NAME, COM_ADDRESS, COM_PHONE, COM_MEMO) "
				   + " VALUES(?, ?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] { companyInformation.getComName(), companyInformation.getComAddress(), companyInformation.getComPhone(), companyInformation.getComMemo() });
		
	}

	@Override
	public void update(CompanyInformation companyInformation) {

		List<Object> args = new ArrayList<Object>();
		
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

	@Override
	public void delete(CompanyInformation companyInformation) {
		
		String sql = " DELETE FROM holiday_dessert.company_information "
				   + " WHERE COM_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { companyInformation.getComId() });
		
	}

	@Override
	public List<Map<String, Object>> frontList(CompanyInformation companyInformation) {
		
		String sql = " SELECT * FROM holiday_dessert.company_information ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

}
