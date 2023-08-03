package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.MemberDao;
import com.holidayDessert.model.Member;

@Repository
public class MemberDaoImpl implements MemberDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Member member) {

		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT * FROM ( "
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

		List<Object> args = new ArrayList<Object>();
		
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
	public void register(Member member) {

		String sql = " INSERT INTO holiday_dessert.member "
				   + " (MEM_NAME, MEM_ACCOUNT, MEM_PASSWORD, MEM_GENDER, MEM_PHONE,"
				   + " MEM_EMAIL, MEM_ADDRESS, MEM_BIRTHDAY, MEM_STATUS) "
				   + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, 0)";
		
		jdbcTemplate.update(sql, new Object[] { member.getMemName(), member.getMemAccount(), member.getMemPassword(),
				member.getMemGender(), member.getMemPhone(), member.getMemEmail(), member.getMemAddress(), member.getMemBirthday() });
		
	}

	@Override
	public void edit(Member member) {

		List<Object> args = new ArrayList<Object>();
		
		String sql = " UPDATE holiday_dessert.member "
				   + " SET MEM_NAME = ?, MEM_ACCOUNT = ?, MEM_PASSWORD = ?, MEM_GENDER = ?, "
				   + " MEM_PHONE = ?, MEM_EMAIL = ?, MEM_ADDRESS = ?, MEM_BIRTHDAY = ? "
				   + " WHERE MEM_ID = ? ";
		
		args.add(member.getMemName());
		args.add(member.getMemAccount());
		args.add(member.getMemPassword());
		args.add(member.getMemGender());
		args.add(member.getMemPhone());
		args.add(member.getMemEmail());
		args.add(member.getMemAddress());
		args.add(member.getMemBirthday());
		args.add(member.getMemId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

}
