package com.holidaydessert.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.Form;
import com.holidaydessert.service.FormService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/form")
@Tag(name = "送單")
public class FormController {

	@Autowired
	private FormService formService;

	@PostMapping(value = "/sendForm")
	@Operation(summary = "送單", description = "送單聯絡我們")
	public ResponseEntity<?> sendForm(
			@Parameter(name = "Form", description = "送單", required = true) @RequestBody Form form) {
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

//	@RequestMapping(value = "/sendForm", method = { RequestMethod.GET, RequestMethod.POST })
//	@Operation(summary = "送單", description = "送單聯絡我們")
//	public ResponseEntity<?> sendForm(
//			@Parameter(name = "formPhone", description = "行動電話", required = true) @RequestParam(description = "formPhone", required = false) String formPhone,
//			@Parameter(name = "formEmail", description = "電子信箱", required = true) @RequestParam(description = "formEmail", required = false) String formEmail,
//			@Parameter(name = "formContent", description = "訊息", required = true) @RequestParam(description = "formContent", required = true) String formContent,
//			@Parameter(name = "formCreateBy", description = "中文姓名", required = true) @RequestParam(description = "formCreateBy", required = true) String formCreateBy) {
//		Form form = new Form(null, formPhone, formEmail, formContent, formCreateBy, null);
//		Map<String, Object> responseMap = new HashMap<>();
//		try {
//			formService.add(form);
//			responseMap.put("STATUS", "T");
//			responseMap.put("MSG", "送出成功");
//		} catch (Exception e) {
//			responseMap.put("STATUS", "F");
//			responseMap.put("MSG", "失敗");
//		}
//		return ResponseEntity.ok(responseMap);
//	}
	
}
