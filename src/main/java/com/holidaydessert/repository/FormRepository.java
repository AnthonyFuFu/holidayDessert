package com.holidaydessert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.holidaydessert.model.Form;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {
    // add 直接使用 JpaRepository 內建的 save()，不需要額外寫任何方法
}