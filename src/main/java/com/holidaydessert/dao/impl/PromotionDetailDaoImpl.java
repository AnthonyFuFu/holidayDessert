package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.PromotionDetailDao;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;

@Repository
public class PromotionDetailDaoImpl implements PromotionDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(PromotionDetail promotionDetail) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT PM_NAME,PM_DESCRIPTION,PM_DISCOUNT,PM_STATUS, "
				   + " DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, "
				   + " DATE_FORMAT(PM_END, '%Y-%m-%d %H:%i:%s') PM_END, "
				   + " PMD_ID, pmd.PD_ID, pmd.PM_ID, PMD_PD_DISCOUNT_PRICE, "
				   + " DATE_FORMAT(PMD_START, '%Y-%m-%d %H:%i:%s') PMD_START, "
				   + " DATE_FORMAT(PMD_END, '%Y-%m-%d %H:%i:%s') PMD_END, "
				   + " PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_STATUS, PD_IS_DEL "
				   + " FROM holiday_dessert.promotion_detail pmd "
				   + " LEFT JOIN product p ON pmd.PD_ID = p.PD_ID "
				   + " LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID ";

		if (promotionDetail.getSearchText() != null && promotionDetail.getSearchText().length() > 0) {
			String[] searchText = promotionDetail.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PM_NAME, ?) > 0"
					+  " OR INSTR(PM_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PM_DISCOUNT, ?) > 0 "
					+  " OR INSTR(PMD_PD_DISCOUNT_PRICE, ?) > 0 "
					+  " OR INSTR(PMD_START, ?) > 0 "
					+  " OR INSTR(PMD_END, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_PRICE, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if(sql.indexOf("WHERE") > 0) {
			sql += " AND PD_IS_DEL = 0 ";
		} else {
			sql += " WHERE PD_IS_DEL = 0 ";
		}
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public int getCount(PromotionDetail promotionDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM holiday_dessert.promotion_detail pmd "
				   + " LEFT JOIN product p ON pmd.PD_ID = p.PD_ID "
				   + " LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID ";
		
		if (promotionDetail.getSearchText() != null && promotionDetail.getSearchText().length() > 0) {
			String[] searchText = promotionDetail.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PM_NAME, ?) > 0"
					+  " OR INSTR(PM_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PM_DISCOUNT, ?) > 0 "
					+  " OR INSTR(PMD_PD_DISCOUNT_PRICE, ?) > 0 "
					+  " OR INSTR(PMD_START, ?) > 0 "
					+  " OR INSTR(PMD_END, ?) > 0 "
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_PRICE, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}

		if(sql.indexOf("WHERE") > 0) {
			sql += " AND PD_IS_DEL = 0 ";
		} else {
			sql += " WHERE PD_IS_DEL = 0 ";
		}
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}

	@Override
	public void addOne(PromotionDetail promotionDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) "
				   + " VALUES(?, ?, CONCAT(?, ' 00:00:00'), CONCAT(?, ' 23:59:59'), ?) ";
		
		args.add(promotionDetail.getPdId());
		args.add(promotionDetail.getPmId());
		args.add(promotionDetail.getPmdStart());
		args.add(promotionDetail.getPmdEnd());
		args.add(promotionDetail.getPmdPdDiscountPrice());
	
		jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void batchAddPromotion(Promotion promotion, String[] productId, String[] productPrice) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) ";
		
		if(productId.length > 0) {
			for(int i=0; i<productId.length; i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(?, ' 00:00:00'), CONCAT(?, ' 23:59:59'), ?) ";
				} else {
					sql += " ,(?, ?, CONCAT(?, ' 00:00:00'), CONCAT(?, ' 23:59:59'), ?) ";
				}
				args.add(productId[i]);
				args.add(promotion.getPmId());
				args.add(promotion.getPmStart());
				args.add(promotion.getPmEnd());
				args.add(Math.round(Double.valueOf(productPrice[i]) * Double.valueOf(promotion.getPmDiscount())* 100.0) / 100.0);
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void batchAddOneDayPromotion(Promotion promotion, String[] productId, String[] productPrice) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) ";
		
		if(productId.length > 0) {
			for(int i=0; i<productId.length; i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), ?) ";
				} else {
					sql += " ,(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), ?) ";
				}
				args.add(productId[i]);
				args.add(promotion.getPmId());
				args.add(Math.round(Double.valueOf(productPrice[i]) * Double.valueOf(promotion.getPmDiscount())* 100.0) / 100.0);
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}
	
	@Override
	public void batchAddOneWeekPromotion(Promotion promotion, String[] productId, String[] productPrice) {
		
		List<Object> args = new ArrayList<>();

		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) ";
		
		if(productId.length > 0) {
			for(int i=0; i<productId.length; i++) {
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), ?) ";
				} else {
					sql += " ,(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), ?) ";
				}
				args.add(productId[i]);
				args.add(promotion.getPmId());
				args.add(Math.round(Double.valueOf(productPrice[i]) * Double.valueOf(promotion.getPmDiscount())* 100.0) / 100.0);
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}

	@Override
	public void update(PromotionDetail promotionDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.promotion_detail "
				   + " SET PD_ID = ?, PM_ID = ?, PMD_START = ?, PMD_END = ?, PMD_PD_DISCOUNT_PRICE = ? "
				   + " WHERE PMD_ID = ? ";
		
		args.add(promotionDetail.getPdId());
		args.add(promotionDetail.getPmId());
		args.add(promotionDetail.getPmdStart());
		args.add(promotionDetail.getPmdEnd());
		args.add(promotionDetail.getPmdPdDiscountPrice());
		args.add(promotionDetail.getPmdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public List<Map<String, Object>> frontList(PromotionDetail promotionDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT PM_NAME,PM_DESCRIPTION,PM_DISCOUNT,PM_STATUS, "
				   + " DATE_FORMAT(PM_START, '%Y-%m-%d %H:%i:%s') PM_START, "
				   + " DATE_FORMAT(PM_END, '%Y-%m-%d %H:%i:%s') PM_END, "
				   + " PMD_ID, pmd.PD_ID, pmd.PM_ID, PMD_PD_DISCOUNT_PRICE, "
				   + " DATE_FORMAT(PMD_START, '%Y-%m-%d %H:%i:%s') PMD_START, "
				   + " DATE_FORMAT(PMD_END, '%Y-%m-%d %H:%i:%s') PMD_END, "
				   + " PD_NAME,PD_PRICE,PD_DESCRIPTION,PD_STATUS, PD_IS_DEL "
				   + " FROM holiday_dessert.promotion_detail pmd "
				   + " LEFT JOIN product p ON pmd.PD_ID = p.PD_ID "
				   + " LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID "
				   + " WHERE pmd.PM_ID = ? "
				   + " AND PD_IS_DEL = 0 ";

		args.add(promotionDetail.getPmId());
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
		
	}

}
