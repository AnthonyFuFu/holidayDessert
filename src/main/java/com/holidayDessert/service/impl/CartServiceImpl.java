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

}
