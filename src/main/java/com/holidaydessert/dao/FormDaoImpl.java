package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Form;

@Repository
public class FormDaoImpl implements FormDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Form form) {
		List<Object> args = new ArrayList<>();

		String sql = " SELECT * FROM holiday_dessert.form ";

		if (form.getSearchText() != null && form.getSearchText().length() > 0) {
			String[] searchText = form.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(FORM_PHONE, ?) > 0"
					+  " OR INSTR(FORM_EMAIL, ?) > 0 "
					+  " OR INSTR(FORM_CONTENT, ?) > 0 "
					+  " OR INSTR(CREATE_BY, ?) > 0 "
					+  " OR INSTR(CREATE_TIME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if (form.getStart() != null && !"".equals(form.getStart())) {
			sql += " LIMIT " + form.getStart() + "," + form.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Form form) {
		List<Object> args = new ArrayList<>();

		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.form ";

		if (form.getSearchText() != null && form.getSearchText().length() > 0) {
			String[] searchText = form.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(FORM_PHONE, ?) > 0"
					+  " OR INSTR(FORM_EMAIL, ?) > 0 "
					+  " OR INSTR(FORM_CONTENT, ?) > 0 "
					+  " OR INSTR(CREATE_BY, ?) > 0 "
					+  " OR INSTR(CREATE_TIME, ?) > 0 "
					+  " ) ";
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
	public void add(Form form) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.form "
				   + " (FORM_PHONE, FORM_EMAIL, FORM_CONTENT, CREATE_BY, CREATE_TIME) "
				   + " VALUES(?, ?, ?, ?, NOW()) ";
		
		args.add(form.getFormPhone());
		args.add(form.getFormEmail());
		args.add(form.getFormContent());
		args.add(form.getFormCreateBy());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

}
