package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.ProductCollection;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollection, String> {

    // =============================================
    // getAllPdcList
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.product_collection " +
                   "WHERE PDC_STATUS = 1",
           nativeQuery = true)
    List<Map<String, Object>> getAllPdcList();

    // =============================================
    // getPdByPdcName
    // =============================================
    @Query(value = "SELECT pdc.PDC_NAME, pdc.PDC_KEYWORD, " +
                   "       p.PD_ID, p.PDC_ID, p.PD_NAME, p.PD_PRICE, p.PD_DESCRIPTION, p.PD_DISPLAY_QUANTITY " +
                   "FROM holiday_dessert.product_collection pdc " +
                   "LEFT JOIN holiday_dessert.product p ON pdc.PDC_ID = p.PDC_ID " +
                   "WHERE PDC_STATUS = 1 " +
                   "AND p.PD_STATUS = 1 " +
                   "AND p.PD_IS_DEL = 0 " +
                   "AND PDC_NAME = :pdcName",
           nativeQuery = true)
    List<Map<String, Object>> getPdByPdcName(@Param("pdcName") String pdcName);
    
}
