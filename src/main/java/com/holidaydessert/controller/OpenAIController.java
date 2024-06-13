package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.ChatGPTService;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

	@Autowired
	private ChatGPTService chatGPTService;
	
	@GetMapping("/chat")
	public ResponseEntity<?> chat(@RequestParam String prompt) {

		ApiReturnObject apiReturnObject = chatGPTService.getChat(prompt);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
	
}
