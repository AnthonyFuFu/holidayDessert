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

@RestController
@RequestMapping("/form")
public class FormController {

	@Autowired
	private FormService formService;
	
	@RequestMapping(value = "/sendForm", method = RequestMethod.POST)
	public ResponseEntity<?> sendForm(@RequestBody Form form) {
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

