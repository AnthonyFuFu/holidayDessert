package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductPicDao;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.service.ProductPicService;

@Service
public class ProductPicServiceImpl implements ProductPicService {

	@Autowired
	private ProductPicDao productPicDao;

	@Override
	public List<Map<String, Object>> list(ProductPic productPic) {
		return productPicDao.list(productPic);
	}

	@Override
	public void add(ProductPic productPic) {
		productPicDao.add(productPic);
	}

	@Override
	public void update(ProductPic productPic) {
		productPicDao.update(productPic);
	}

	@Override
	public void delete(ProductPic productPic) {
		productPicDao.delete(productPic);
	}

	@Override
	public List<Map<String, Object>> frontRandList(ProductPic productPic) {
		return productPicDao.frontRandList(productPic);
	}

}
