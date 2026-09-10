package com.holidaydessert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.holidaydessert.model.EditLog;

public interface EditLogRepository extends JpaRepository<EditLog, String> {

}
