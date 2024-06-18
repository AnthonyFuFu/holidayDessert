package com.holidaydessert.controller;

import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.OcrService;

import net.sourceforge.tess4j.TesseractException;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

	@Autowired
	private OcrService ocrService;
	
	@PostMapping("/recognizeText")
	public ResponseEntity<?> recognizeText(@RequestParam("file") MultipartFile file) {
		try {
			ApiReturnObject apiReturnObject = ocrService.recognizeText(ImageIO.read(file.getInputStream()));
			return ResponseEntity.ok(apiReturnObject);
		} catch (IOException | TesseractException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
