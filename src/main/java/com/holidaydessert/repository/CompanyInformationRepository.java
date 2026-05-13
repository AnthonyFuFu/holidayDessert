package com.holidaydessert.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.CompanyInformation;

@Repository
public interface CompanyInformationRepository extends JpaRepository<CompanyInformation, String> {

    @Query(value = "SELECT * FROM holiday_dessert.company_information",
           nativeQuery = true)
    List<Map<String, Object>> frontList();

}