package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.CompanyInformation;

@Repository
public interface CompanyInformationRepository extends JpaRepository<CompanyInformation, String> {

    // =============================================
    // findDataById：後台單筆查詢（修改頁使用）- 公司資訊
    // SELECT * 直接對應 Entity，之後新增欄位只要在 Entity 加上 @Column 即可
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.company_information " +
                   "WHERE COM_ID = :comId ",
           nativeQuery = true)
    Optional<CompanyInformation> findDataById(@Param("comId") String comId);

    // =============================================
    // backList：後台 DataTables 分頁 - 公司資訊
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT * FROM holiday_dessert.company_information " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(COM_NAME, :keyword) > 0 OR INSTR(COM_ADDRESS, :keyword) > 0 OR INSTR(COM_PHONE, :keyword) > 0 OR INSTR(COM_MEMO, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT * FROM holiday_dessert.company_information " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(COM_NAME, :keyword) > 0 OR INSTR(COM_ADDRESS, :keyword) > 0 OR INSTR(COM_PHONE, :keyword) > 0 OR INSTR(COM_MEMO, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM holiday_dessert.company_information",
           nativeQuery = true)
    List<Map<String, Object>> frontList();

}