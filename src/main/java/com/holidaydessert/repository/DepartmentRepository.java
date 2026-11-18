package com.holidaydessert.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    // =============================================
    // backList：後台 DataTables 分頁 - 部門
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT * FROM holiday_dessert.department " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(DEPT_NAME, :keyword) > 0 OR INSTR(DEPT_LOC, :keyword) > 0) ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT * FROM holiday_dessert.department " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(DEPT_NAME, :keyword) > 0 OR INSTR(DEPT_LOC, :keyword) > 0) ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);
}
