package com.holidaydessert.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.Map;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // =============================================
    // backList：後台 DataTables 分頁 - 員工（在職）
    // 排序由 Pageable 的 Sort 動態產生，SQL 內不寫 ORDER BY
    // =============================================
    @Query(value =
               "SELECT * FROM ( " +
               "  SELECT emp.EMP_ID, emp.DEPT_ID, EMP_NAME, EMP_PHONE, EMP_JOB, EMP_SALARY, EMP_PICTURE, EMP_IMAGE, " +
               "         EMP_ACCOUNT, EMP_EMAIL, EMP_LEVEL, EMP_STATUS, DATE_FORMAT(EMP_HIREDATE, '%Y-%m-%d') EMP_HIREDATE, " +
               "         dept.DEPT_NAME, dept.DEPT_LOC " +
               "  FROM holiday_dessert.employee emp " +
               "  LEFT JOIN holiday_dessert.department dept ON dept.DEPT_ID = emp.DEPT_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(EMP_NAME, :keyword) > 0 OR INSTR(EMP_JOB, :keyword) > 0 OR INSTR(EMP_SALARY, :keyword) > 0 OR INSTR(EMP_PHONE, :keyword) > 0 OR INSTR(EMP_ACCOUNT, :keyword) > 0 OR INSTR(EMP_HIREDATE, :keyword) > 0 OR INSTR(EMP_EMAIL, :keyword) > 0 OR INSTR(DEPT_NAME, :keyword) > 0 OR INSTR(DEPT_LOC, :keyword) > 0) AND EMP_STATUS = 1 ",
           countQuery =
               "SELECT COUNT(*) FROM ( " +
               "  SELECT emp.EMP_ID, emp.DEPT_ID, EMP_NAME, EMP_PHONE, EMP_JOB, EMP_SALARY, EMP_PICTURE, EMP_IMAGE, " +
               "         EMP_ACCOUNT, EMP_EMAIL, EMP_LEVEL, EMP_STATUS, DATE_FORMAT(EMP_HIREDATE, '%Y-%m-%d') EMP_HIREDATE, " +
               "         dept.DEPT_NAME, dept.DEPT_LOC " +
               "  FROM holiday_dessert.employee emp " +
               "  LEFT JOIN holiday_dessert.department dept ON dept.DEPT_ID = emp.DEPT_ID " +
               ") AS subquery " +
               "WHERE (:keyword = '' OR INSTR(EMP_NAME, :keyword) > 0 OR INSTR(EMP_JOB, :keyword) > 0 OR INSTR(EMP_SALARY, :keyword) > 0 OR INSTR(EMP_PHONE, :keyword) > 0 OR INSTR(EMP_ACCOUNT, :keyword) > 0 OR INSTR(EMP_HIREDATE, :keyword) > 0 OR INSTR(EMP_EMAIL, :keyword) > 0 OR INSTR(DEPT_NAME, :keyword) > 0 OR INSTR(DEPT_LOC, :keyword) > 0) AND EMP_STATUS = 1 ",
           nativeQuery = true)
    Page<Map<String, Object>> backList(@Param("keyword") String keyword, Pageable pageable);
	
    @Query("SELECT e FROM Employee e INNER JOIN FETCH e.department")
    List<Employee> findAllWithDepartment();
    
}