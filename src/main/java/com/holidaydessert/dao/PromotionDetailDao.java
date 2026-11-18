package com.holidaydessert.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Product;
import com.holidaydessert.model.Promotion;
import com.holidaydessert.model.PromotionDetail;
import com.holidaydessert.service.ProductService;

@Repository
public class PromotionDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ProductService productService;
	
	// =============================================
	// back
	// =============================================
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

	public void batchAddPromotion(Promotion promotion, String[] productId) {

		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) ";
		
		if(productId.length > 0) {
			for(int i=0; i<productId.length; i++) {
				Product product = new Product();
				product.setPdId(productId[i]);
				product = productService.getData(product);
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(?, ' 00:00:00'), CONCAT(?, ' 23:59:59'), ?) ";
				} else {
					sql += " ,(?, ?, CONCAT(?, ' 00:00:00'), CONCAT(?, ' 23:59:59'), ?) ";
				}
				args.add(productId[i]);
				args.add(promotion.getPmId());
				args.add(promotion.getPmStart());
				args.add(promotion.getPmEnd());
				args.add(Math.round(Double.valueOf(product.getPdPrice()) * Double.valueOf(promotion.getPmDiscount())* 100.0) / 100.0);
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}

	public void batchAddOneDayPromotion(Promotion promotion, String[] productId) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) ";
		
		if(productId.length > 0) {
			for(int i=0; i<productId.length; i++) {
				Product product = new Product();
				product.setPdId(productId[i]);
				product = productService.getData(product);
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), ?) ";
				} else {
					sql += " ,(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 DAY), ' 23:59:59'), ?) ";
				}
				args.add(productId[i]);
				args.add(promotion.getPmId());
				args.add(Math.round(Double.valueOf(product.getPdPrice()) * Double.valueOf(promotion.getPmDiscount())* 100.0) / 100.0);
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}
	
	public void batchAddOneWeekPromotion(Promotion promotion, String[] productId) {
		
		List<Object> args = new ArrayList<>();

		String sql = " INSERT INTO holiday_dessert.promotion_detail "
				   + " (PD_ID, PM_ID, PMD_START, PMD_END, PMD_PD_DISCOUNT_PRICE) ";
		
		if(productId.length > 0) {
			for(int i=0; i<productId.length; i++) {
				Product product = new Product();
				product.setPdId(productId[i]);
				product = productService.getData(product);
				if(i == 0) {
					sql += " VALUES(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), ?) ";
				} else {
					sql += " ,(?, ?, CONCAT(CURDATE(), ' 00:00:00'), CONCAT(DATE_ADD(CURDATE(), INTERVAL 1 WEEK), ' 23:59:59'), ?) ";
				}
				args.add(productId[i]);
				args.add(promotion.getPmId());
				args.add(Math.round(Double.valueOf(product.getPdPrice()) * Double.valueOf(promotion.getPmDiscount())* 100.0) / 100.0);
			}
		}
		jdbcTemplate.update(sql, args.toArray());
	}

	public void update(PromotionDetail promotionDetail) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.promotion_detail "
				   + " SET PD_ID = ?, PM_ID = ?, PMD_START = CONCAT(?, ' 00:00:00'), PMD_END = CONCAT(?, ' 23:59:59'), PMD_PD_DISCOUNT_PRICE = ? "
				   + " WHERE PMD_ID = ? ";
		
		args.add(promotionDetail.getPdId());
		args.add(promotionDetail.getPmId());
		args.add(promotionDetail.getPmdStart());
		args.add(promotionDetail.getPmdEnd());
		args.add(promotionDetail.getPmdPdDiscountPrice());
		args.add(promotionDetail.getPmdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}
	
}
