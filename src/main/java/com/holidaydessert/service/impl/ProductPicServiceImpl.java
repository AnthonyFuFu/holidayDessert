package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ProductPicDao;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.repository.ProductPicRepository;
import com.holidaydessert.service.ProductPicService;

@Service
public class ProductPicServiceImpl implements ProductPicService {

	@Autowired
	private ProductPicDao productPicDao;
	
	@Autowired
	private ProductPicRepository productPicRepository;
	
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
	public ProductPic getData(ProductPic productPic) {
		return productPicDao.getData(productPic);
	}

	// =============================================
	// frontRandList
	// =============================================
	@Override
	public List<Map<String, Object>> frontRandList(ProductPic productPic) {
	    List<Map<String, Object>> list;

	    if (productPic.getLength() != null && !"".equals(productPic.getLength())) {
	        // 有 LIMIT：用 Pageable 處理
	        int length = Integer.parseInt(productPic.getLength());
	        Pageable pageable = PageRequest.of(0, length);
	        list = productPicRepository.frontRandList(productPic.getPdId(), pageable);
	    } else {
	        // 無 LIMIT：查全部
	        list = productPicRepository.frontRandListAll(productPic.getPdId());
	    }

	    return list.isEmpty() ? null : list;
	}

}
