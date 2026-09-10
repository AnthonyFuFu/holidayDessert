package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.CartDao;
import com.holidaydessert.model.Cart;
import com.holidaydessert.model.Cart.CartId;
import com.holidaydessert.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	private CartDao cartDao;
	
    @Autowired
    private CartRepository cartRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Cart cart) {
		return cartDao.list(cart);
	}

	public Integer getCount(Cart cart) {
		return cartDao.getCount(cart);
	}

	// =============================================
	// front
	// =============================================
	public List<Cart> frontList(String memId) {
        List<Cart> list = cartRepository.frontList(memId);
        return list.isEmpty() ? null : list;
	}

    @Transactional
    public void insert(Cart cart) {
        cart.setId(new CartId(cart.getMemId(), cart.getPdId()));
        cartRepository.insert(
            cart.getMemId(),
            cart.getPdId(),
            cart.getCartPdQuantity()
        );
    }

    @Transactional
    public void update(Cart cart) {
        cartRepository.update(
            cart.getMemId(),
            cart.getPdId(),
            cart.getCartPdQuantity()
        );
    }

    @Transactional
    public void delete(Cart cart) {
        cartRepository.delete(
            cart.getMemId(),
            cart.getPdId()
        );
    }

}
