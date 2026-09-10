package com.holidaydessert.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.model.EditLog;
import com.holidaydessert.repository.EditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditLogService {

	@Autowired
    private EditLogRepository editLogRepository;

    public void addLog(EditLog editLog) {
        editLogRepository.save(editLog);
    }
    
}