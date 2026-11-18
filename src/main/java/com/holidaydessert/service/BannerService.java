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
		// 查無資料時回傳空的 Banner（Controller 會直接取用欄位）
		Banner data = bannerRepository.findDataById(banner.getBanId()).orElseGet(Banner::new);
		// newsName 為 @Transient，由關聯的最新消息帶出
		if (data.getNews() != null) {
			data.setNewsName(data.getNews().getNewsName());
		}
		return data;
	}

	// =============================================
	// front
	// =============================================
	public List<Banner> frontRandList(String newsId) {
		return bannerRepository.findFrontRandList(newsId);
	}
	
}
