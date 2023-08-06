package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ReceiptInformationDao;
import com.holidaydessert.model.ReceiptInformation;

@Repository
public class ReceiptInformationDaoImpl implements ReceiptInformationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(ReceiptInformation receiptInformation) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.receipt_information ";

		if (receiptInformation.getSearchText() != null && receiptInformation.getSearchText().length() > 0) {
			String[] searchText = receiptInformation.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(RCP_NAME, ?) > 0"
					+  " OR INSTR(RCP_CVS, ?) > 0 "
					+  " OR INSTR(RCP_ADDRESS, ?) > 0 "
					+  " OR INSTR(RCP_PHONE, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if (receiptInformation.getStart() != null && !"".equals(receiptInformation.getStart())) {
			sql += " LIMIT " + receiptInformation.getStart() + "," + receiptInformation.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(ReceiptInformation receiptInformation) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.receipt_information ";

		if (receiptInformation.getSearchText() != null && receiptInformation.getSearchText().length() > 0) {
			String[] searchText = receiptInformation.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(RCP_NAME, ?) > 0"
					+  " OR INSTR(RCP_CVS, ?) > 0 "
					+  " OR INSTR(RCP_ADDRESS, ?) > 0 "
					+  " OR INSTR(RCP_PHONE, ?) > 0 "
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
	public List<Map<String, Object>> frontList(ReceiptInformation receiptInformation) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.receipt_information "
				   + " WHERE MEM_ID = ? ";

		args.add(receiptInformation.getMemId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public void add(ReceiptInformation receiptInformation) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.receipt_information "
				   + " (MEM_ID, RCP_NAME, RCP_CVS, RCP_ADDRESS, RCP_PHONE) "
				   + " VALUES(?, ?, ?, ?, ?) ";
		
		args.add(receiptInformation.getMemId());
		args.add(receiptInformation.getRcpName());
		args.add(receiptInformation.getRcpCvs());
		args.add(receiptInformation.getRcpAddress());
		args.add(receiptInformation.getRcpPhone());
		
		jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void edit(ReceiptInformation receiptInformation) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.receipt_information "
				   + " SET RCP_NAME = ?, RCP_CVS = ?, RCP_ADDRESS = ?, RCP_PHONE = ? "
				   + " WHERE RCP_ID = ? ";
		
		args.add(receiptInformation.getRcpName());
		args.add(receiptInformation.getRcpCvs());
		args.add(receiptInformation.getRcpAddress());
		args.add(receiptInformation.getRcpPhone());
		args.add(receiptInformation.getRcpId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

}
