package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.model.ReceiptInformation;

@Repository
public interface ReceiptInformationRepository extends JpaRepository<ReceiptInformation, String> {

    // =============================================
    // frontList
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.receipt_information " +
                   "WHERE MEM_ID = :memId",
           nativeQuery = true)
    List<Map<String, Object>> frontList(@Param("memId") String memId);

    // =============================================
    // add → save() 內建，不需要額外方法
    // =============================================

    // =============================================
    // edit
    // =============================================
    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.receipt_information " +
                   "SET RCP_NAME = :rcpName, RCP_CVS = :rcpCvs, " +
                   "    RCP_ADDRESS = :rcpAddress, RCP_PHONE = :rcpPhone " +
                   "WHERE RCP_ID = :rcpId",
           nativeQuery = true)
    void edit(@Param("rcpId")      String rcpId,
              @Param("rcpName")    String rcpName,
              @Param("rcpCvs")     String rcpCvs,
              @Param("rcpAddress") String rcpAddress,
              @Param("rcpPhone")   String rcpPhone);
}