package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    // =============================================
    // frontOrderDetails
    // =============================================
    @Query(value = "SELECT od.*, PD_NAME " +
                   "FROM holiday_dessert.order_detail od " +
                   "LEFT JOIN main_order mo ON od.ORD_ID = mo.ORD_ID " +
                   "LEFT JOIN product p ON p.PD_ID = od.PD_ID " +
                   "WHERE od.ORD_ID = :ordId",
           nativeQuery = true)
    List<Map<String, Object>> frontOrderDetails(@Param("ordId") String ordId);

    // =============================================
    // add → save() 內建，不需要額外方法
    // =============================================
}
