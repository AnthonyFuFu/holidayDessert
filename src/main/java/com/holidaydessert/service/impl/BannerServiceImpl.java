package com.holidaydessert.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.BannerDao;
import com.holidaydessert.model.Banner;
import com.holidaydessert.service.BannerService;

@Service
public class BannerServiceImpl implements BannerService{
	
	@Autowired
	private BannerDao bannerDao;
	
	@Override
	public List<Map<String, Object>> list(Banner banner) {
		return bannerDao.list(banner);
	}

	@Override
	public void add(Banner banner) {
		bannerDao.add(banner);
	}

	@Override
	public void update(Banner banner) {
		bannerDao.update(banner);
	}

	@Override
	public void delete(Banner banner) {
		bannerDao.delete(banner);
	}

	@Override
	public Banner getData(Banner banner) {
		return bannerDao.getData(banner);
	}
	
	@Override
	public List<Map<String, Object>> frontRandList(Banner banner) {
		return bannerDao.frontRandList(banner);
	}
	
}
