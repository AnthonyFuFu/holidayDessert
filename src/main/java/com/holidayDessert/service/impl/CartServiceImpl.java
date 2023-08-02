package com.holidayDessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidayDessert.dao.CartDao;
import com.holidayDessert.model.Cart;
import com.holidayDessert.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartDao cartDao;
	
	@Override
	public List<Map<String, Object>> list(Cart cart) {
		return cartDao.list(cart);
	}

	@Override
	public int getCount(Cart cart) {
		return cartDao.getCount(cart);
	}

	@Override
	public void add(Cart cart) {
		cartDao.add(cart);
	}

	@Override
	public void update(Cart cart) {
		cartDao.update(cart);
	}

	@Override
	public void delete(Cart cart) {
		cartDao.delete(cart);
	}

}
