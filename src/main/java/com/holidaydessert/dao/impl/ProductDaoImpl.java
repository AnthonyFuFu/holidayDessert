package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, "
				   + " PD_DISPLAY_QUANTITY, PD_STATUS, PD_IS_DEL, PD_CREATE_BY, PD_UPDATE_BY, "
				   + " DATE_FORMAT(PD_CREATE_TIME, '%Y-%m-%d %H:%i:%s') PD_CREATE_TIME, "
				   + " DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.product "
				   + ") AS subquery ";
		
		if (product.getSearchText() != null && product.getSearchText().length() > 0) {
			String[] searchText = product.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_ID, ?) > 0"
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_PRICE, ?) > 0 "
					+  " OR INSTR(PD_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PD_DISPLAY_QUANTITY, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if(sql.indexOf("WHERE") > 0) {
			sql += " AND PD_IS_DEL = 0 ";
		} else {
			sql += " WHERE PD_IS_DEL = 0 ";
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT *, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.product "
				   + ") AS subquery ";

		if (product.getSearchText() != null && product.getSearchText().length() > 0) {
			String[] searchText = product.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_ID, ?) > 0"
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_PRICE, ?) > 0 "
					+  " OR INSTR(PD_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PD_DISPLAY_QUANTITY, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if(sql.indexOf("WHERE") > 0) {
			sql += " AND PD_IS_DEL = 0 ";
		} else {
			sql += " WHERE PD_IS_DEL = 0 ";
		}
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void add(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " INSERT INTO holiday_dessert.employee "
				   + " (PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, PD_STATUS, "
				   + " PD_IS_DEL, PD_CREATE_BY, PD_CREATE_TIME, PD_UPDATE_BY, PD_UPDATE_TIME) "
				   + " VALUES(?, ?, ?, ?, ?, ?, 0, ?, NOW(), ?, NOW()) ";
		
		args.add(product.getPdcId());
		args.add(product.getPdName());
		args.add(product.getPdPrice());
		args.add(product.getPdDescription());
		args.add(product.getPdDisplayQuantity());
		args.add(product.getPdStatus());
		args.add(product.getPdCreateBy());
		args.add(product.getPdUpdateBy());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.product "
				   + " SET PDC_ID = ?, PD_NAME = ?, PD_PRICE = ?, PD_DESCRIPTION = ?, "
				   + " PD_DISPLAY_QUANTITY = ?, PD_STATUS = ?, PD_IS_DEL = ?, PD_UPDATE_BY = ?, PD_UPDATE_TIME = NOW() "
				   + " WHERE PD_ID = ? ";
		
		args.add(product.getPdcId());
		args.add(product.getPdName());
		args.add(product.getPdPrice());
		args.add(product.getPdDescription());
		args.add(product.getPdDisplayQuantity());
		args.add(product.getPdStatus());
		args.add(product.getPdIsDel());
		args.add(product.getPdUpdateBy());
		args.add(product.getPdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void delete(Product product) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.product "
				   + " SET PD_IS_DEL = 1, PD_UPDATE_BY = ?, PD_UPDATE_TIME = NOW() "
				   + " WHERE PD_ID = ? ";
		
		args.add(product.getPdUpdateBy());
		args.add(product.getPdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public List<Map<String, Object>> frontNewList(Product product) {

		String sql = " SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, "
				   + " PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PIC "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID "
				   + " WHERE PD_STATUS = 1 "
				   + " AND PD_IS_DEL = 0 "
				   + " AND (p.PD_ID, PD_PIC_SORT) IN ( "
				   + " SELECT PD_ID, MIN(PD_PIC_SORT) "
				   + " FROM product_pic "
				   + " GROUP BY PD_ID) "
				   + " ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME)) ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> frontTypeList(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, "
				   + " PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PIC "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID "
				   + " WHERE PD_STATUS = 1 "
				   + " AND PD_IS_DEL = 0 "
				   + " AND (p.PD_ID, PD_PIC_SORT) IN ( "
				   + " SELECT PD_ID, MIN(PD_PIC_SORT) "
				   + " FROM product_pic "
				   + " GROUP BY PD_ID) "
				   + " AND PDC_ID = ? "
				   + " ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME)) ";

		args.add(product.getPdcId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, "
				   + " PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PIC "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID "
				   + " WHERE PD_STATUS = 1 "
				   + " AND PD_IS_DEL = 0 "
				   + " AND (p.PD_ID, PD_PIC_SORT) IN ( "
				   + " SELECT PD_ID, MIN(PD_PIC_SORT) "
				   + " FROM product_pic "
				   + " GROUP BY PD_ID) "
				   + " AND PDC_ID = ? "
				   + " ORDER BY RAND() ";

		args.add(product.getPdcId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

}