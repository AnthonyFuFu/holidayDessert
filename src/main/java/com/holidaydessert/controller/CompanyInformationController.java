package com.holidaydessert.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/companyInformation")
@Tag(name = "公司資訊")
public class CompanyInformationController {
	
	@RequestMapping(value = "/index" , method = {RequestMethod.GET, RequestMethod.POST})
	@Operation(summary = "獲取公司資訊", description = "顯示公司詳細資料")
    public ResponseEntity<?> companyInformation(HttpSession session) {
    	Map<String, Object> responseMap = new HashMap<>();
        try {
        	responseMap.put("STATUS", "Y");
        	responseMap.put("MSG", "成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            responseMap.put("STATUS", "N");
            responseMap.put("MSG", "錯誤");
        }
        return ResponseEntity.ok(responseMap);
    }
	
}
