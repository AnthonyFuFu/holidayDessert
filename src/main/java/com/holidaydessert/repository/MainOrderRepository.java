package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.MainOrder;

@Repository
public interface MainOrderRepository extends JpaRepository<MainOrder, String> {

    // =============================================
    // add → 使用 JpaRepository 內建 save()，不需要額外方法
    // =============================================
	
    // =============================================
    // getMemOrderList
    // =============================================
    @Query(value = " SELECT MEM_ACCOUNT, " +
                   " CASE MEM_GENDER " +
                   " WHEN 'm' THEN '男' " +
                   " WHEN 'f' THEN '女' " +
                   " END AS MEM_GENDER, " +
                   " MEM_EMAIL, mo.*, " +
                   " CASE WHEN mo.MEM_CP_ID IS NULL THEN '未使用' " +
                   " ELSE (SELECT CP_NAME " +
                   " FROM member_coupon mc " +
                   " LEFT JOIN coupon c ON c.CP_ID = mc.CP_ID " +
                   " WHERE mc.MEM_CP_ID = mo.MEM_CP_ID " +
                   " LIMIT 1) " +
                   " END AS COUPON_USED " +
                   " FROM holiday_dessert.main_order mo " +
                   " LEFT JOIN member m ON m.MEM_ID = mo.MEM_ID " +
                   " WHERE mo.MEM_ID = :memId",
           nativeQuery = true)
    List<Map<String, Object>> getMemOrderList(@Param("memId") Integer memId);
    
}
