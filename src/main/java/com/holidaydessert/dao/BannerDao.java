package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Banner;

@Repository
public class BannerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// =============================================
	// back
	// =============================================
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

	public void delete(Banner banner) {
		
		String sql = " DELETE FROM holiday_dessert.banner "
				   + " WHERE BAN_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { banner.getBanId() });
		
	}

}
