package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.constant.SortFieldsConstant;
import com.holidaydessert.model.Cart.CartId;
import com.holidaydessert.model.Cart;
import com.holidaydessert.repository.CartRepository;
import com.holidaydessert.utils.PageableUtil;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

	// =============================================
	// back
	// =============================================
	public Map<String, Object> list(Map<String, String> dataTableParams) {
		// 1. 取得 DataTables 參數
		String keyword = PageableUtil.getSearchValue(dataTableParams);
		String draw = PageableUtil.getDraw(dataTableParams);
		// 2. 建立分頁與排序（start / length / order）
		Pageable pageable = PageableUtil.buildDataTablePageable(
				dataTableParams,
				SortFieldsConstant.CART_TABLE,
				SortFieldsConstant.MEMBER_DEFAULT,
				Sort.Direction.ASC
		);
		// 3. 查詢資料
		Page<Map<String, Object>> cartPage = cartRepository.backList(keyword, pageable);
		// 4. 封裝成 DataTables 回傳格式
		return PageableUtil.buildDataTable(cartPage, PageableUtil.toMutableList(cartPage), draw);
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
