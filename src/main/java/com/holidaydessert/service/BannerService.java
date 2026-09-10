package com.holidaydessert.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.BannerDao;
import com.holidaydessert.model.Banner;
import com.holidaydessert.repository.BannerRepository;

@Service
public class BannerService {

	@Autowired
	private BannerDao bannerDao;

    @Autowired
    private BannerRepository bannerRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> list(Banner banner) {
		return bannerDao.list(banner);
	}

	public void add(Banner banner) {
		bannerDao.add(banner);
	}

	public void update(Banner banner) {
		bannerDao.update(banner);
	}

	public void delete(Banner banner) {
		bannerDao.delete(banner);
	}

	public Banner getData(Banner banner) {
		return bannerDao.getData(banner);
	}

	// =============================================
	// front
	// =============================================
	public List<Banner> frontRandList(String newsId) {
		return bannerRepository.findFrontRandList(newsId);
	}
	
}
