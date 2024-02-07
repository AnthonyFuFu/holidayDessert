package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.CartDao;
import com.holidaydessert.model.Cart;
import com.holidaydessert.model.Cart.CartId;

@Repository
public class CartDaoImpl implements CartDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;
    
	@Override
	public List<Map<String, Object>> list(Cart cart) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT m.MEM_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, "
				   + " MEM_EMAIL, CART_PD_QUANTITY, PD_NAME, PD_IS_DEL, "
				   + " CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER "
				   + " FROM holiday_dessert.cart c "
				   + " LEFT JOIN product p ON p.PD_ID = c.PD_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = c.MEM_ID "
				   + " ) AS subquery ";
		
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
					+  " OR INSTR(GENDER, ?) > 0 "
					+  " OR INSTR(MEM_PHONE, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
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
	public Integer getCount(Cart cart) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT m.MEM_ID, MEM_NAME, MEM_ACCOUNT, MEM_GENDER, MEM_PHONE, "
				   + " MEM_EMAIL,CART_PD_QUANTITY, PD_NAME, PD_IS_DEL, "
				   + " CASE MEM_GENDER WHEN 'm' THEN '男' WHEN 'f' THEN '女' END AS GENDER "
				   + " FROM holiday_dessert.cart c "
				   + " LEFT JOIN product p ON p.PD_ID = c.PD_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = c.MEM_ID "
				   + ") AS subquery ";
		
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
					+  " OR INSTR(GENDER, ?) > 0 "
					+  " OR INSTR(MEM_PHONE, ?) > 0 "
					+  " OR INSTR(MEM_EMAIL, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
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
	public List<Map<String, Object>> frontList(Cart cart) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT m.MEM_ID,MEM_NAME,MEM_ACCOUNT,MEM_GENDER,MEM_PHONE,MEM_EMAIL,CART_PD_QUANTITY,p.* "
				   + " FROM holiday_dessert.cart c "
				   + " LEFT JOIN product p ON p.PD_ID = c.PD_ID "
				   + " LEFT JOIN member m ON m.MEM_ID = c.MEM_ID "
				   + " WHERE c.MEM_ID = ? "
				   + " AND PD_IS_DEL = 0 "
				   + " AND PD_STATUS = 1 ";
		
		args.add(cart.getMemId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
    @Transactional
	public void add(Cart cart) {
	    cart.setId(new CartId(cart.getMemId(), cart.getPdId()));
		entityManager.persist(cart);
	}

	@Override
    @Transactional
	public void update(Cart cart) {
		
		cart.setId(new CartId(cart.getMemId(), cart.getPdId()));
	    Cart managedCart = entityManager.find(Cart.class, cart.getId());
	    
	    if (managedCart != null) {
	        managedCart.setCartPdQuantity(cart.getCartPdQuantity());
	        entityManager.merge(managedCart);
	    }
	    
	}

	@Override
    @Transactional
	public void delete(Cart cart) {
		
		cart.setId(new CartId(cart.getMemId(), cart.getPdId()));
		Cart managedCart = entityManager.find(Cart.class, cart.getId());
		
		if (managedCart != null) {
			entityManager.remove(managedCart);
		}
		
	}
	
}