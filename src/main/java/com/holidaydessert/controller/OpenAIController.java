package com.holidaydessert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.ChatGPTService;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

	@Autowired
	private ChatGPTService chatGPTService;
	
	@PostMapping("/chat")
	public ResponseEntity<?> chat(@RequestParam String prompt) {
		ApiReturnObject apiReturnObject = chatGPTService.getChat(prompt);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}
	
	
	// 以下功能要升級成更高階的才能使用 is not supported with the current subscription key and pricing tier OpenAI.S0.
	@RequestMapping(value = "/generateCaptionForImage", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> generateCaptionForImage(@RequestParam("file") MultipartFile file) {
		ApiReturnObject apiReturnObject = chatGPTService.generateCaptionForImage(file);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}

	@RequestMapping(value = "/generateCaptionForURL", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> generateCaptionForURL(@RequestParam String url) {
//		url = "https://aka.ms/azsdk/image-analysis/sample.jpg";
		ApiReturnObject apiReturnObject = chatGPTService.generateCaptionForURL(url);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}

	@RequestMapping(value = "/extractTextFromImage", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> extractTextFromImage(@RequestParam("file") MultipartFile file) {
		ApiReturnObject apiReturnObject = chatGPTService.extractTextFromImage(file);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}

	@RequestMapping(value = "/extractTextFromURL", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<?> extractTextFromURL(@RequestParam String url) {
//		url = "https://aka.ms/azsdk/image-analysis/sample.jpg";
		ApiReturnObject apiReturnObject = chatGPTService.extractTextFromURL(url);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}
	
}
