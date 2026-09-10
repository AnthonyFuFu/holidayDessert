package com.holidaydessert.service;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.holidaydessert.model.ApiReturnObject;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OcrService {

	private final ITesseract tesseract;
	
	public OcrService() {
		this.tesseract = new Tesseract();
		this.tesseract.setDatapath("tessdata");
	}

	public ApiReturnObject recognizeText(BufferedImage image) throws TesseractException {

//		tesseract.setLanguage("chi_sim+chi_tra"); // 混用
		tesseract.setLanguage("chi_tra");
		tesseract.setOcrEngineMode(3); // 設置OCR引擎模式
		String replyContent = tesseract.doOCR(image);
		return ApiReturnObject.success("取得對話成功", replyContent);

	}
	
}
