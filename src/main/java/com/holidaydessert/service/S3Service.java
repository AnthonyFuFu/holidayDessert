package com.holidaydessert.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.config.S3Properties;
import com.holidaydessert.utils.S3Util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3Service {

    @Autowired
    private S3Properties s3Properties;
    
    @Autowired
    private S3Util s3Util;
    
	@Transactional(rollbackFor = Exception.class)
	public String uploadFile(MultipartFile file, String key) throws IOException {

		// 1. 上傳最新檔案至 S3
		s3Util.putObject(s3Properties.getBucketName(), key, file);
		
		return key;
	}

	public String downloadFile(String key, HttpServletRequest httpRequest) {
		// 產生 Presigned URL
		String downloadUrl = s3Util.getPresignedGetObject(s3Properties.getBucketName(), key);
		return downloadUrl;
	}
	
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String key) {
        // 1. 從 S3 刪除
    	s3Util.deleteObject(s3Properties.getBucketName(), key);
        // 2. 從資料庫刪除紀錄
        log.info("資料庫紀錄刪除成功: {}", key);
    }
    
}
