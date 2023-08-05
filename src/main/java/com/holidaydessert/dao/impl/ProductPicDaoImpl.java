package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ProductPicDao;
import com.holidaydessert.model.ProductPic;

@Repository
public class ProductPicDaoImpl implements ProductPicDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(ProductPic productPic) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.product_pic "
				   + " WHERE PD_ID = ? ";

		args.add(productPic.getPdId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public void add(ProductPic productPic) {

		String sql = " INSERT INTO holiday_dessert.product_pic "
				   + " (PD_ID, PD_PIC) "
				   + " VALUES(?, ?) ";
		
		jdbcTemplate.update(sql, new Object[] {productPic.getPdId(), productPic.getPdPic() });
		
	}

	@Override
	public void delete(ProductPic productPic) {

		String sql = " DELETE FROM holiday_dessert.product_pic "
				   + " WHERE PD_PIC_ID = ? ";
		
		jdbcTemplate.update(sql, new Object[] { productPic.getPdPicId() });
		
	}

	@Override
	public List<Map<String, Object>> frontRandList(ProductPic productPic) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.product_pic "
				   + " WHERE PD_ID = ? "
				   + " ORDER BY RAND() ";

		if (productPic.getLength() != null && !"".equals(productPic.getLength())) {
			sql += " LIMIT " + productPic.getLength();
		}

		args.add(productPic.getPdId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

}
