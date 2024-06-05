package com.holidaydessert.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.Form;
import com.holidaydessert.service.FormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/form")
@Api(tags = "送單")
public class FormController {

	@Autowired
	private FormService formService;
	
	@RequestMapping(value = "/sendForm", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "送單", httpMethod = "POST", notes = "送單聯絡我們")
	public ResponseEntity<?> sendForm(@ApiParam(name = "Form", value = "送單", required = true)@RequestBody Form form) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			formService.add(form);
			responseMap.put("STATUS", "T");
            responseMap.put("MSG", "送出成功");
		} catch (Exception e) {
			responseMap.put("STATUS", "F");
			responseMap.put("MSG", "失敗");
		}
		return ResponseEntity.ok(responseMap);
	}
	
}

