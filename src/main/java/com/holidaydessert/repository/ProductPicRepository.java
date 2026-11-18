package com.holidaydessert.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.ProductPic;

@Repository
public interface ProductPicRepository extends JpaRepository<ProductPic, String> {

    // =============================================
    // findDataById：後台單筆查詢（修改頁使用）- 商品圖片
    // SELECT * 直接對應 Entity，之後新增欄位只要在 Entity 加上 @Column 即可
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.product_pic " +
                   "WHERE PD_PIC_ID = :pdPicId ",
           nativeQuery = true)
    Optional<ProductPic> findDataById(@Param("pdPicId") String pdPicId);

	// =============================================
	// frontRandList：帶 LIMIT（Pageable 處理動態 LIMIT）
	// =============================================
	@Query(value = "SELECT pp.* FROM holiday_dessert.product_pic pp WHERE pp.PD_ID = :pdId ORDER BY RAND()",
			countQuery = "SELECT COUNT(*) FROM holiday_dessert.product_pic pp WHERE pp.PD_ID = :pdId",
			nativeQuery = true)
	Page<ProductPic> frontRandList(@Param("pdId") String pdId, Pageable pageable);

}