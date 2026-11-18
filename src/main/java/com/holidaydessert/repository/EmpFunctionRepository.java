package com.holidaydessert.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.EmpFunction;

@Repository
public interface EmpFunctionRepository extends JpaRepository<EmpFunction, String> {

    // =============================================
    // findDataById：後台單筆查詢（修改頁使用）- 功能選單
    // SELECT * 直接對應 Entity，之後新增欄位只要在 Entity 加上 @Column 即可
    // =============================================
    @Query(value = "SELECT * FROM holiday_dessert.emp_function " +
                   "WHERE FUNC_ID = :funcId ",
           nativeQuery = true)
    Optional<EmpFunction> findDataById(@Param("funcId") String funcId);

    // =============================================
    // backList：後台 DataTables 分頁 - 功能選單（啟用中）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT * FROM holiday_dessert.emp_function " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(FUNC_NAME, :keyword) > 0 OR INSTR(FUNC_LINK, :keyword) > 0 OR INSTR(FUNC_ICON, :keyword) > 0) AND FUNC_STATUS = 1 ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT * FROM holiday_dessert.emp_function " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(FUNC_NAME, :keyword) > 0 OR INSTR(FUNC_LINK, :keyword) > 0 OR INSTR(FUNC_ICON, :keyword) > 0) AND FUNC_STATUS = 1 ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);
}
