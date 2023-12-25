package com.holidaydessert.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holidaydessert.dao.ProductDao;
import com.holidaydessert.model.Product;

@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> list(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( SELECT PD_ID, p.PDC_ID, PDC_NAME, PD_NAME, PD_PRICE, PD_DESCRIPTION, "
				   + " PD_DISPLAY_QUANTITY, PD_STATUS, PD_IS_DEL, PD_CREATE_BY, PD_UPDATE_BY, "
				   + " DATE_FORMAT(PD_CREATE_TIME, '%Y-%m-%d %H:%i:%s') PD_CREATE_TIME, "
				   + " DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_collection pc ON p.PDC_ID = pc.PDC_ID "
				   + ") AS subquery ";
		
		if (product.getSearchText() != null && product.getSearchText().length() > 0) {
			String[] searchText = product.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_NAME, ?) > 0"
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_PRICE, ?) > 0 "
					+  " OR INSTR(PD_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PD_DISPLAY_QUANTITY, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
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

		if (product.getStart() != null && !"".equals(product.getStart())) {
			sql += " LIMIT " + product.getStart() + "," + product.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public int getCount(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( SELECT p.*, pc.PDC_NAME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_collection pc ON p.PDC_ID = pc.PDC_ID "
				   + ") AS subquery ";

		if (product.getSearchText() != null && product.getSearchText().length() > 0) {
			String[] searchText = product.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_NAME, ?) > 0"
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " OR INSTR(PD_PRICE, ?) > 0 "
					+  " OR INSTR(PD_DESCRIPTION, ?) > 0 "
					+  " OR INSTR(PD_DISPLAY_QUANTITY, ?) > 0 "
					+  " OR INSTR(STATUS, ?) > 0 "
					+  " ) ";
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
	public void add(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " INSERT INTO holiday_dessert.product "
				   + " (PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, PD_STATUS, "
				   + " PD_IS_DEL, PD_CREATE_BY, PD_CREATE_TIME, PD_UPDATE_BY, PD_UPDATE_TIME) "
				   + " VALUES(?, ?, ?, ?, ?, ?, 0, ?, NOW(), ?, NOW()) ";
		
		args.add(product.getPdcId());
		args.add(product.getPdName());
		args.add(product.getPdPrice());
		args.add(product.getPdDescription());
		args.add(product.getPdDisplayQuantity());
		args.add(product.getPdStatus());
		args.add(product.getPdCreateBy());
		args.add(product.getPdUpdateBy());

		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void update(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.product "
				   + " SET PDC_ID = ?, PD_NAME = ?, PD_PRICE = ?, PD_DESCRIPTION = ?, "
				   + " PD_DISPLAY_QUANTITY = ?, PD_STATUS = ?, PD_IS_DEL = ?, PD_UPDATE_BY = ?, PD_UPDATE_TIME = NOW() "
				   + " WHERE PD_ID = ? ";
		
		args.add(product.getPdcId());
		args.add(product.getPdName());
		args.add(product.getPdPrice());
		args.add(product.getPdDescription());
		args.add(product.getPdDisplayQuantity());
		args.add(product.getPdStatus());
		args.add(product.getPdIsDel());
		args.add(product.getPdUpdateBy());
		args.add(product.getPdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public void delete(Product product) {
		
		List<Object> args = new ArrayList<>();
		
		String sql = " UPDATE holiday_dessert.product "
				   + " SET PD_IS_DEL = 1, PD_UPDATE_BY = ?, PD_UPDATE_TIME = NOW() "
				   + " WHERE PD_ID = ? ";
		
		args.add(product.getPdUpdateBy());
		args.add(product.getPdId());
		
		jdbcTemplate.update(sql, args.toArray());
		
	}

	@Override
	public Product getData(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.product "
				   + " WHERE PD_ID = ? ";
		
		args.add(product.getPdId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		Product item = new Product();
		if (!list.isEmpty()) {
	        Map<String, Object> resultMap = list.get(0);
	        String pdId = String.valueOf(resultMap.get("PD_ID"));
	        String pdcId = String.valueOf(resultMap.get("PDC_ID"));
	        String pdName = String.valueOf(resultMap.get("PD_NAME"));
	        String pdPrice = String.valueOf(resultMap.get("PD_PRICE"));
	        String pdDescription = String.valueOf(resultMap.get("PD_DESCRIPTION"));
	        String pdDisplayQuantity = String.valueOf(resultMap.get("PD_DISPLAY_QUANTITY"));
	        String pdStatus = String.valueOf(resultMap.get("PD_STATUS"));
	        String pdCreateBy = String.valueOf(resultMap.get("PD_CREATE_BY"));
	        String pdCreateTime = String.valueOf(resultMap.get("PD_CREATE_TIME"));
	        String pdUpdateBy = String.valueOf(resultMap.get("PD_UPDATE_BY"));
	        String pdUpdateTime = String.valueOf(resultMap.get("PD_UPDATE_TIME"));
	        
	        item.setPdId(pdId);
	        item.setPdcId(pdcId);
	        item.setPdName(pdName);
	        item.setPdPrice(pdPrice);
	        item.setPdDescription(pdDescription);
	        item.setPdDisplayQuantity(pdDisplayQuantity);
	        item.setPdStatus(pdStatus);
	        item.setPdCreateBy(pdCreateBy);
	        item.setPdCreateTime(pdCreateTime);
	        item.setPdUpdateBy(pdUpdateBy);
	        item.setPdUpdateTime(pdUpdateTime);
	    }
		return item == null ? null : item;
	}

	@Override
	public List<Map<String, Object>> getList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM holiday_dessert.product "
				   + " WHERE PD_IS_DEL = 0 ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public List<Map<String, Object>> getPicList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT PD_ID, p.PDC_ID, PDC_NAME, PD_NAME, PD_PRICE, PD_DESCRIPTION, "
				   + " PD_DISPLAY_QUANTITY, PD_STATUS, PD_IS_DEL, PD_CREATE_BY, PD_UPDATE_BY "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_collection pc ON p.PDC_ID = pc.PDC_ID "
				   + " WHERE PD_IS_DEL = 0 ";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}
	
	@Override
	public List<Map<String, Object>> issuePromotionList(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( "
				   + " SELECT sub.PD_ID, sub.PM_ID, sub.PDC_NAME, sub.PD_NAME, sub.PD_DESCRIPTION, sub.PD_PRICE, sub.PMD_START, sub.PMD_END "
				   + " FROM (SELECT p.PD_ID, pmd.PM_ID, PDC_NAME, PD_NAME, PD_DESCRIPTION, PD_PRICE, "
				   + " DATE_FORMAT(PMD_START, '%Y-%m-%d') AS PMD_START, "
				   + " DATE_FORMAT(PMD_END, '%Y-%m-%d') AS PMD_END, "
				   + " ROW_NUMBER() OVER (PARTITION BY p.PD_ID ORDER BY PMD_END DESC) AS rn "
				   + " FROM product p "
				   + " LEFT JOIN product_collection pc ON p.PDC_ID = pc.PDC_ID "
				   + " LEFT JOIN promotion_detail pmd ON p.PD_ID = pmd.PD_ID "
				   + " LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID "
				   + " WHERE p.PD_STATUS = 1 "
				   + " AND p.PD_IS_DEL = 0 "
				   + " ) AS sub "
				   + " WHERE sub.rn = 1 "
				   + " AND (CURDATE() < PMD_START OR CURDATE() > PMD_END OR PMD_START IS NULL OR PMD_END IS NULL) "
				   + " GROUP BY sub.PD_ID "
				   + " ) AS subquery ";
		
		if (product.getSearchText() != null && product.getSearchText().length() > 0){
			String[] searchText = product.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_NAME, ?) > 0"
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		if (product.getStart() != null && !"".equals(product.getStart())) {
			sql += " LIMIT " + product.getStart() + "," + product.getLength();
		}

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}

	@Override
	public List<Map<String, Object>> issueOneProductList() {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT * FROM ( "
				   + " SELECT sub.PD_ID, sub.PM_ID, sub.PDC_NAME, sub.PD_NAME, sub.PD_DESCRIPTION, sub.PD_PRICE, sub.PMD_START, sub.PMD_END "
				   + " FROM (SELECT p.PD_ID, pmd.PM_ID, PDC_NAME, PD_NAME, PD_DESCRIPTION, PD_PRICE, "
				   + " DATE_FORMAT(PMD_START, '%Y-%m-%d') AS PMD_START, "
				   + " DATE_FORMAT(PMD_END, '%Y-%m-%d') AS PMD_END, "
				   + " ROW_NUMBER() OVER (PARTITION BY p.PD_ID ORDER BY PMD_END DESC) AS rn "
				   + " FROM product p "
				   + " LEFT JOIN product_collection pc ON p.PDC_ID = pc.PDC_ID "
				   + " LEFT JOIN promotion_detail pmd ON p.PD_ID = pmd.PD_ID "
				   + " LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID "
				   + " WHERE p.PD_STATUS = 1 "
				   + " AND p.PD_IS_DEL = 0 "
				   + " ) AS sub "
				   + " WHERE sub.rn = 1 "
				   + " AND (CURDATE() < PMD_START OR CURDATE() > PMD_END OR PMD_START IS NULL OR PMD_END IS NULL) "
				   + " GROUP BY sub.PD_ID "
				   + " ) AS subquery ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}

	}
	
	@Override
	public int getIssuePromotionCount(Product product) {

		List<Object> args = new ArrayList<>();
		
		String sql = " SELECT COUNT(*) AS COUNT "
				   + " FROM ( "
				   + " SELECT sub.PD_ID, sub.PM_ID, sub.PDC_NAME, sub.PD_NAME, sub.PD_DESCRIPTION, sub.PD_PRICE, sub.PMD_START, sub.PMD_END "
				   + " FROM (SELECT p.PD_ID, pmd.PM_ID, PDC_NAME, PD_NAME, PD_DESCRIPTION, PD_PRICE, "
				   + " DATE_FORMAT(PMD_START, '%Y-%m-%d') AS PMD_START, "
				   + " DATE_FORMAT(PMD_END, '%Y-%m-%d') AS PMD_END, "
				   + " ROW_NUMBER() OVER (PARTITION BY p.PD_ID ORDER BY PMD_END DESC) AS rn "
				   + " FROM product p "
				   + " LEFT JOIN product_collection pc ON p.PDC_ID = pc.PDC_ID "
				   + " LEFT JOIN promotion_detail pmd ON p.PD_ID = pmd.PD_ID "
				   + " LEFT JOIN promotion pm ON pmd.PM_ID = pm.PM_ID "
				   + " WHERE p.PD_STATUS = 1 "
				   + " AND p.PD_IS_DEL = 0 "
				   + " ) AS sub "
				   + " WHERE sub.rn = 1 "
				   + " AND (CURDATE() < PMD_START OR CURDATE() > PMD_END OR PMD_START IS NULL OR PMD_END IS NULL) "
				   + " GROUP BY sub.PD_ID "
				   + " ) AS subquery ";
		
		if (product.getSearchText() != null && product.getSearchText().length() > 0){
			String[] searchText = product.getSearchText().split(" ");
			sql += " WHERE ";
			for(int i=0; i<searchText.length; i++) {
				if(i > 0) {
					sql += " AND ( ";
				} else {
					sql += " ( ";
				}
				sql += " INSTR(PDC_NAME, ?) > 0"
					+  " OR INSTR(PD_NAME, ?) > 0 "
					+  " ) ";
		  		args.add(searchText[i]);
		  		args.add(searchText[i]);
			}
		}
		
		return Integer.valueOf(jdbcTemplate.queryForList(sql, args.toArray()).get(0).get("COUNT").toString());
	}
	
	@Override
	public List<Map<String, Object>> frontNewList(Product product) {

		String sql = " SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, "
				   + " PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PIC "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID "
				   + " WHERE PD_STATUS = 1 "
				   + " AND PD_IS_DEL = 0 "
				   + " AND (p.PD_ID, PD_PIC_SORT) IN ( "
				   + " SELECT PD_ID, MIN(PD_PIC_SORT) "
				   + " FROM product_pic "
				   + " GROUP BY PD_ID) "
				   + " ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME)) ";
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> frontTypeList(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, "
				   + " PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PIC "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID "
				   + " WHERE PD_STATUS = 1 "
				   + " AND PD_IS_DEL = 0 "
				   + " AND (p.PD_ID, PD_PIC_SORT) IN ( "
				   + " SELECT PD_ID, MIN(PD_PIC_SORT) "
				   + " FROM product_pic "
				   + " GROUP BY PD_ID) "
				   + " AND PDC_ID = ? "
				   + " ORDER BY ABS(TIMESTAMPDIFF(SECOND, NOW(), PD_UPDATE_TIME)) ";

		args.add(product.getPdcId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}

	@Override
	public List<Map<String, Object>> frontRandTypeList(Product product) {

		List<Object> args = new ArrayList<>();

		String sql = " SELECT p.PD_ID, PDC_ID, PD_NAME, PD_PRICE, PD_DESCRIPTION, PD_DISPLAY_QUANTITY, "
				   + " PD_STATUS, DATE_FORMAT(PD_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') PD_UPDATE_TIME, "
				   + " CASE PD_STATUS "
				   + " WHEN '0' THEN '下架' "
				   + " WHEN '1' THEN '上架' "
				   + " END AS STATUS, PD_PIC_ID, PD_PIC_SORT, PD_PIC "
				   + " FROM holiday_dessert.product p "
				   + " LEFT JOIN product_pic ppic ON p.PD_ID = ppic.PD_ID "
				   + " WHERE PD_STATUS = 1 "
				   + " AND PD_IS_DEL = 0 "
				   + " AND (p.PD_ID, PD_PIC_SORT) IN ( "
				   + " SELECT PD_ID, MIN(PD_PIC_SORT) "
				   + " FROM product_pic "
				   + " GROUP BY PD_ID) "
				   + " AND PDC_ID = ? "
				   + " ORDER BY RAND() ";

		args.add(product.getPdcId());
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args.toArray());
		
		if(list!=null && list.size()>0) {
			return list;
		} else {
			return null;
		}
		
	}
	
}
