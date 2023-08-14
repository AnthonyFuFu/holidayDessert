package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.CartDao;
import com.holidaydessert.model.Cart;
import com.holidaydessert.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartDao cartDao;
	
	@Override
	public List<Map<String, Object>> list(Cart cart) {
		return cartDao.list(cart);
	}

	@Override
	public Integer getCount(Cart cart) {
		return cartDao.getCount(cart);
	}
	
	@Override
	public List<Map<String, Object>> frontList(Cart cart) {
		return cartDao.frontList(cart);
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
