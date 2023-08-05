package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ProductCollectionDao;
import com.holidaydessert.model.ProductCollection;

@Repository
public class ProductCollectionDaoImpl implements ProductCollectionDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(ProductCollection productCollection) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT *,"
				   + " CASE PDC_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.product_collection "
				   + " ) AS subquery ";

		if (productCollection.getSearchText() != null && productCollection.getSearchText().length() > 0) {
			String[] searchText = productCollection.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_NAME, ?) > 0"
					+  " OR INSTR(PDC_KEYWORD, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if (productCollection.getStart() != null && !"".equals(productCollection.getStart())) {
			sql += " LIMIT " + productCollection.getStart() + "," + productCollection.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(ProductCollection productCollection) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT *,"
				   + " CASE PDC_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.product_collection "
				   + " ) AS subquery ";

		if (productCollection.getSearchText() != null && productCollection.getSearchText().length() > 0) {
			String[] searchText = productCollection.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_NAME, ?) > 0"
					+  " OR INSTR(PDC_KEYWORD, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(ProductCollection productCollection) {

		String sql = " INSERT INTO holiday_dessert.product_collection "
				   + " (PDC_NAME, PDC_KEYWORD, PDC_STATUS) "
				   + " VALUES(?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] { productCollection.getPdcName(), productCollection.getPdcKeyword(), productCollection.getPdcStatus() });
		
	}

	@Override
	public void update(ProductCollection productCollection) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.product_collection "
				   + " SET PDC_NAME = ?, PDC_KEYWORD = ?, PDC_STATUS = ? "
				   + " WHERE PDC_ID = ? ";
		
		args.add(productCollection.getPdcName());
		args.add(productCollection.getPdcKeyword());
		args.add(productCollection.getPdcStatus());
		args.add(productCollection.getPdcId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void takeDown(ProductCollection productCollection) {

		String sql = " UPDATE holiday_dessert.product_collection "
				   + " SET PDC_STATUS = 0 "
				   + " WHERE PDC_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { productCollection.getPdcId() });
		
	}

	@Override
	public List<Map<String, Object>> frontList(ProductCollection productCollection) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.product_collection "
				   + " WHERE PDC_STATUS = 1 ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

}
