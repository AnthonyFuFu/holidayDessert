package com.holidaydessert.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.ApiReturnObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import io.swagger.annotations.Api;

@RestController
@Api(tags = { "提取PDF文字" })
public class PdfController {
	
	@PostMapping("/extractPDF")
	public ResponseEntity<?> extractPDF(@RequestParam("file") MultipartFile file) {
		try {
			String pdfText = extractTextFromPDF(file.getInputStream());
			System.out.println(pdfText);
			ApiReturnObject apiReturnObject = new ApiReturnObject(200, "取得對話成功", pdfText);
			return ResponseEntity.ok(apiReturnObject);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	public static String extractTextFromPDF(InputStream inputStream) {
		StringBuilder text = new StringBuilder();
		try {
			PdfReader reader = new PdfReader(inputStream);
			int numberOfPages = reader.getNumberOfPages();
			for (int i = 1; i <= numberOfPages; i++) {
				text.append(PdfTextExtractor.getTextFromPage(reader, i));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text.toString();
	}
	
//	@RequestMapping(value = "/extractPDF", method = { RequestMethod.GET, RequestMethod.POST })
////	public ResponseEntity<?> extractPDF() {
//	public ResponseEntity<?> extractPDF(@RequestParam("file") MultipartFile file) {
//		try {
//			String pdfText = extractTextFromPDF("C:\\Users\\TKB0004757\\Downloads\\生物課程報告-分裂組探究實驗教學.pdf");
//			System.out.println(pdfText);
//			ApiReturnObject apiReturnObject =  new ApiReturnObject(200, "取得對話成功", pdfText);
//			return ResponseEntity.ok(apiReturnObject);
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}
//	public static String extractTextFromPDF(String filePath) {
//		StringBuilder text = new StringBuilder();
//		try {
//			PdfReader reader = new PdfReader(filePath);
//			int numberOfPages = reader.getNumberOfPages();
//			for (int i = 1; i <= numberOfPages; i++) {
//				text.append(PdfTextExtractor.getTextFromPage(reader, i));
//			}
//			reader.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return text.toString();
//	}

}
