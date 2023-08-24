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
				   + " WHERE PD_ID = ? "
				   + " ORDER BY PD_PIC_SORT ";

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

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.product_pic "
				   + " (PD_ID, PD_PIC_SORT, PD_PICTURE, PD_IMAGE) "
				   + " VALUES(?, ?, ?, ?) ";
		
		args.add(productPic.getPdId());
		args.add(productPic.getPdPicSort());
		args.add(productPic.getPdPicture());
		args.add(productPic.getPdImage());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(ProductPic productPic) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.product_pic "
				   + " SET PD_PIC_SORT = ?, PD_PICTURE = ?, PD_IMAGE = ? "
				   + " WHERE PD_PIC_ID = ? ";
		
		args.add(productPic.getPdPicSort());
		args.add(productPic.getPdPicture());
		args.add(productPic.getPdImage());
		args.add(productPic.getPdPicId());
		
		jdbcTemplate.update(sql, args.toArray());
		
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
