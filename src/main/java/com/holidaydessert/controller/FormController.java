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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/form")
@Api(tags = "送單")
public class FormController {

	@Autowired
	private FormService formService;

	@PostMapping(value = "/sendForm")
	@ApiOperation(value = "送單", notes = "送單聯絡我們")
	public ResponseEntity<?> sendForm(
			@ApiParam(name = "Form", value = "送單", required = true) @RequestBody Form form) {
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
//	@ApiOperation(value = "送單", httpMethod = "POST", notes = "送單聯絡我們")
//	public ResponseEntity<?> sendForm(
//			@ApiParam(name = "formPhone", value = "行動電話", required = true) @RequestParam(value = "formPhone", required = false) String formPhone,
//			@ApiParam(name = "formEmail", value = "電子信箱", required = true) @RequestParam(value = "formEmail", required = false) String formEmail,
//			@ApiParam(name = "formContent", value = "訊息", required = true) @RequestParam(value = "formContent", required = true) String formContent,
//			@ApiParam(name = "formCreateBy", value = "中文姓名", required = true) @RequestParam(value = "formCreateBy", required = true) String formCreateBy) {
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
