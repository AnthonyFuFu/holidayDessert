package com.holidaydessert.service;

import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.ApiReturnObject;

public interface ChatGPTService {
	
	public ApiReturnObject getChat(String prompt);
	public ApiReturnObject generateCaptionForImage(MultipartFile file);
	public ApiReturnObject generateCaptionForURL(String url);
	public ApiReturnObject extractTextFromImage(MultipartFile file);
	public ApiReturnObject extractTextFromURL(String url);
	
}
