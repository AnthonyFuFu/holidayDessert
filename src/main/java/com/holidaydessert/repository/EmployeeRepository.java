package com.holidaydessert.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
	
    @Query("SELECT e FROM Employee e INNER JOIN FETCH e.department")
    List<Employee> findAllWithDepartment();
    
}