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
		
		String sql = " INSERT INTO holiday_dessert.banner "
				   + " (NEWS_ID, BAN_PIC) "
				   + " VALUES(?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] {banner.getNewsId(), banner.getBanPic() });
		
	}

	@Override
	public void delete(Banner banner) {
		
		String sql = " DELETE FROM holiday_dessert.banner "
				   + " WHERE BAN_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { banner.getBanId() });
		
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