package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.BannerDao;
import com.holidaydessert.model.Banner;

@Repository
public class BannerDaoImpl implements BannerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Banner banner) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.banner "
				   + " WHERE NEWS_ID = ? ";
		
		args.add(banner.getNewsId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public void add(Banner banner) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.banner "
				   + " (NEWS_ID, BAN_PICTURE, BAN_IMAGE) "
				   + " VALUES(?, ?, ?) ";

		args.add(banner.getNewsId());
		args.add(banner.getBanPicture());
		args.add(banner.getBanImage());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(Banner banner) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.banner "
				   + " SET BAN_PICTURE = ?, BAN_IMAGE = ? "
				   + " WHERE BAN_ID = ? ";
		
		args.add(banner.getBanPicture());
		args.add(banner.getBanImage());
		args.add(banner.getBanId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void delete(Banner banner) {
		
		String sql = " DELETE FROM holiday_dessert.banner "
				   + " WHERE BAN_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { banner.getBanId() });
		
	}

	@Override
	public Banner getData(Banner banner) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT b.*,n.NEWS_NAME FROM holiday_dessert.banner b "
				   + " LEFT JOIN news n on n.NEWS_ID = b.NEWS_ID "
				   + " WHERE BAN_ID = ? ";
		
		args.add(banner.getBanId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		Banner pic = new Banner();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        
	        String banId = String.valueOf(resultMap.get("BAN_ID"));
	        String newsId = String.valueOf(resultMap.get("NEWS_ID"));
	        String banPicture = String.valueOf(resultMap.get("BAN_PICTURE"));
	        String banImage = String.valueOf(resultMap.get("BAN_IMAGE"));
	        String newsName = String.valueOf(resultMap.get("NEWS_NAME"));
	        
	        pic.setBanId(banId);
	        pic.setNewsId(newsId);
	        pic.setBanPicture(banPicture);
	        pic.setBanImage(banImage);
	        pic.setNewsName(newsName);
	        
	    }
		return pic == null ? null : pic;
	}

	@Override
	public List<Map<String, Object>> frontRandList(Banner banner) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.banner "
				   + " WHERE NEWS_ID = ? "
				   + " ORDER BY RAND() LIMIT 3 ";
		
		args.add(banner.getNewsId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

}