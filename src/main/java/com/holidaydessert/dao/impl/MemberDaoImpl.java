package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.MemberDao;
import com.holidaydessert.model.Member;

@Repository
public class MemberDaoImpl implements MemberDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> list(Member member) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( "
				   + " SELECT MEM_ID, MEM_NAME, MEM_ACCOUNT, MEM_PASSWORD, MEM_GENDER, MEM_PHONE, "
				   + " MEM_EMAIL, MEM_ADDRESS, DATE_FORMAT(MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, "
				   + " MEM_STATUS, MEM_VERIFICATION_STATUS, MEM_VERIFICATION_CODE, "
				   + " CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER "
				   + " FROM holiday_dessert.member "
				   + ") AS subquery ";
		
		if (member.getSearchText() != null && member.getSearchText().length() > 0) {
			String[] searchText = member.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(MEM_ACCOUNT, ?) > 0 "
					+  " OR INSTR(MEM_PHONE, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(MEM_ADDRESS, ?) > 0 "
					+  " OR INSTR(MEM_BIRTHDAY, ?) > 0 "
					+  " OR INSTR(GENDER, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if (member.getStart() != null && !"".equals(member.getStart())) {
			sql += " LIMIT " + member.getStart() + "," + member.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Member member) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( "
				   + " SELECT *,CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER "
				   + " FROM holiday_dessert.member "
				   + ") AS subquery ";

		if (member.getSearchText() != null && member.getSearchText().length() > 0) {
			String[] searchText = member.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(MEM_ACCOUNT, ?) > 0 "
					+  " OR INSTR(MEM_PHONE, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(MEM_ADDRESS, ?) > 0 "
					+  " OR INSTR(MEM_BIRTHDAY, ?) > 0 "
					+  " OR INSTR(GENDER, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public List<Map<String, Object>> issueCouponList(Member member) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( "
				   + " SELECT m.MEM_ID, m.MEM_NAME, "
				   + " CASE m.MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER, "
				   + " m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, DATE_FORMAT(m.MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, SUM(mo.ORD_TOTAL) AS TOTAL_EXPENSE "
				   + " FROM member m "
				   + " INNER JOIN main_order mo ON m.MEM_ID = mo.MEM_ID "
				   + " WHERE mo.ORD_CREATE >= DATE_SUB(NOW(), INTERVAL 1 MONTH) "
				   + " AND m.MEM_STATUS = 1 "
				   + " AND m.MEM_VERIFICATION_STATUS = 1 "
				   + " GROUP BY m.MEM_ID, m.MEM_NAME, m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, m.MEM_GENDER "
				   + " ) AS subquery ";
		
		if (member.getTotalExpense() != null && member.getSearchText() != null && member.getSearchText().length() > 0) {
			
			sql += " WHERE TOTAL_EXPENSE >= " + member.getTotalExpense();
			
		} else if (member.getTotalExpense() == null && member.getSearchText() != null && member.getSearchText().length() > 0){
			String[] searchText = member.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(GENDER, ?) > 0 "
					+  " OR INSTR(MEM_BIRTHDAY, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if (member.getStart() != null && !"".equals(member.getStart())) {
			sql += " LIMIT " + member.getStart() + "," + member.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getIssueCouponCount(Member member) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( "
				   + " SELECT m.MEM_ID, m.MEM_NAME, "
				   + " CASE m.MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER, "
				   + " m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, DATE_FORMAT(m.MEM_BIRTHDAY, '%Y-%m-%d') MEM_BIRTHDAY, SUM(mo.ORD_TOTAL) AS TOTAL_EXPENSE "
				   + " FROM member m "
				   + " INNER JOIN main_order mo ON m.MEM_ID = mo.MEM_ID "
				   + " WHERE mo.ORD_CREATE >= DATE_SUB(NOW(), INTERVAL 1 MONTH) "
				   + " AND m.MEM_STATUS = 1 "
				   + " AND m.MEM_VERIFICATION_STATUS = 1 "
				   + " GROUP BY m.MEM_ID, m.MEM_NAME, m.MEM_ACCOUNT, m.MEM_PHONE, m.MEM_EMAIL, m.MEM_GENDER "
				   + " ) AS subquery ";

		if (member.getTotalExpense() != null && member.getSearchText() != null && member.getSearchText().length() > 0) {

			sql += " WHERE TOTAL_EXPENSE >= " + member.getTotalExpense();

		} else if (member.getTotalExpense() == null && member.getSearchText() != null && member.getSearchText().length() > 0) {
			String[] searchText = member.getSearchText().split(" ");
			sql += " WHERE ";
			for (int i = 0; i < searchText.length; i++) {
				if (i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(GENDER, ?) > 0 "
					+  " OR INSTR(MEM_BIRTHDAY, ?) > 0 "
					+  " ) ";
				args.add(searchText[i]);
				args.add(searchText[i]);
				args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}
	
}
