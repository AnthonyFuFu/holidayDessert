package com.holidaydessert.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/companyInformation")
@Api(tags = { "公司資訊" })
public class CompanyInformationController {
	
	@RequestMapping(value = "/index" , method = {RequestMethod.GET, RequestMethod.POST})
	@ApiOperation(value = "獲取公司資訊", httpMethod = "POST", notes = "顯示公司詳細資料")
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
