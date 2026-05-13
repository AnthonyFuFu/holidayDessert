package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.CartDao;
import com.holidaydessert.model.Cart;
import com.holidaydessert.model.Cart.CartId;
import com.holidaydessert.repository.CartRepository;
import com.holidaydessert.service.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartDao cartDao;
	
    @Autowired
    private CartRepository cartRepository;
    
	@Override
	public List<Map<String, Object>> list(Cart cart) {
		return cartDao.list(cart);
	}

	@Override
	public Integer getCount(Cart cart) {
		return cartDao.getCount(cart);
	}
	
	@Override
	public List<Cart> frontList(String memId) {
        List<Cart> list = cartRepository.frontList(memId);
        return list.isEmpty() ? null : list;
	}

    @Override
    @Transactional
    public void insert(Cart cart) {
        cart.setId(new CartId(cart.getMemId(), cart.getPdId()));
        cartRepository.insert(
            cart.getMemId(),
            cart.getPdId(),
            cart.getCartPdQuantity()
        );
    }

    @Override
    @Transactional
    public void update(Cart cart) {
        cartRepository.update(
            cart.getMemId(),
            cart.getPdId(),
            cart.getCartPdQuantity()
        );
    }

    @Override
    @Transactional
    public void delete(Cart cart) {
        cartRepository.delete(
            cart.getMemId(),
            cart.getPdId()
        );
    }

}
