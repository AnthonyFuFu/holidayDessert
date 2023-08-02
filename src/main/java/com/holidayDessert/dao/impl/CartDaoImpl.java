package com.holidayDessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidayDessert.dao.CartDao;
import com.holidayDessert.model.Cart;

@Repository
public class CartDaoImpl implements CartDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Cart cart) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT m.MEM_ID,MEM_NAME,MEM_ACCOUNT,MEM_GENDER,MEM_PHONE,MEM_EMAIL,CART_PD_QUANTITY,p.* "
				   + " FROM holiday_dessert.cart c "
				   + " LEFT JOIN product p ON p.PD_ID = c.PD_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = c.MEM_ID ";
		
		if (cart.getSearchText() != null && cart.getSearchText().length() > 0) {
			String[] searchText = cart.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(MEM_ACCOUNT, ?) > 0 "
					+  " OR INSTR(MEM_GENDER, ?) > 0 "
					+  " OR INSTR(MEM_PHONE, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_DESCRIPTION, ?) > 0 "
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
		
		if (cart.getStart() != null && !"".equals(cart.getStart())) {
			sql += " LIMIT " + cart.getStart() + "," + cart.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public Integer count(Cart cart) {
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.cart c "
				   + " LEFT JOIN product p ON p.PD_ID = c.PD_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = c.MEM_ID ";
		
		if (cart.getSearchText() != null && cart.getSearchText().length() > 0) {
			String[] searchText = cart.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(MEM_NAME, ?) > 0"
					+  " OR INSTR(MEM_ACCOUNT, ?) > 0 "
					+  " OR INSTR(MEM_GENDER, ?) > 0 "
					+  " OR INSTR(MEM_PHONE, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_DESCRIPTION, ?) > 0 "
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
	public List<Map<String, Object>> frontList(Cart cart) {
		
		List<Object> args = new ArrayList<Object>();
		
		String sql = " SELECT m.MEM_ID,MEM_NAME,MEM_ACCOUNT,MEM_GENDER,MEM_PHONE,MEM_EMAIL,CART_PD_QUANTITY,p.* "
				   + " FROM holiday_dessert.cart c "
				   + " LEFT JOIN product p ON p.PD_ID = c.PD_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = c.MEM_ID "
				   + " WHERE c.MEM_ID = ? ";
		
		args.add(cart.getMemId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public void add(Cart cart) {
		String sql = " INSERT INTO holiday_dessert.cart "
				   + " (MEM_ID, PD_ID, CART_PD_QUANTITY) "
				   + " VALUES(?, ?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] { cart.getMemId(), cart.getPdId(), cart.getCartPdQuantity() });
		
	}

	@Override
	public void update(Cart cart) {

		List<Object> args = new ArrayList<Object>();
		
		String sql = " UPDATE holiday_dessert.cart "
				   + " SET CART_PD_QUANTITY = ? "
				   + " WHERE MEM_ID = ? "
				   + " AND PD_ID = ? ";
		
		args.add(cart.getCartPdQuantity());
		args.add(cart.getMemId());
		args.add(cart.getPdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void delete(Cart cart) {

		String sql = " DELETE FROM holiday_dessert.cart "
				   + " WHERE MEM_ID = ? "
				   + " AND PD_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { cart.getMemId(), cart.getPdId() });
		
	}

}