package com.holidaydessert.service;

import java.awt.image.BufferedImage;

import com.holidaydessert.model.ApiReturnObject;

import net.sourceforge.tess4j.TesseractException;

public interface OcrService {
	
	public ApiReturnObject recognizeText(BufferedImage image) throws TesseractException;
	
}
