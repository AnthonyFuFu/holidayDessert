package com.holidaydessert.service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.config.S3Properties;
import com.holidaydessert.model.PendingUpload;
import com.holidaydessert.utils.S3Util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.CompletedPart;

@Slf4j
@Service
public class S3Service {

	@Autowired
	private S3Properties s3Properties;

	@Autowired
	private S3Util s3Util;

	@Autowired
	private PendingUploadRegistry pendingUploadRegistry;

	@Transactional(rollbackFor = Exception.class)
	public String uploadFile(MultipartFile file, String key) throws IOException {
		s3Util.putObject(s3Properties.getBucketName(), key, file);
		return key;
	}

	public String downloadFile(String key, HttpServletRequest httpRequest) {
		return s3Util.getPresignedGetObject(s3Properties.getBucketName(), key);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteFile(String key) {
		s3Util.deleteObject(s3Properties.getBucketName(), key);
		log.info("S3 檔案刪除成功: {}", key);
	}

	// 第 1 步：建立 S3 multipart upload，並暫存 uploadId 對應的 objectKey。
	public String initMultipartUpload(String key, String contentType) {
		if (key == null || key.trim().isEmpty()) {
			throw new IllegalArgumentException("S3 key 不可為空");
		}
		key = key.trim();
		String uploadId = s3Util.createMultipartUpload(s3Properties.getBucketName(), key, contentType);
		pendingUploadRegistry.register(uploadId, new PendingUpload(key));

		log.info("S3 分片上傳初始化成功: key={}, uploadId={}", key, uploadId);
		return uploadId;
	}

	// 第 2 步：上傳單一分片，並保存 S3 回傳的 ETag。
	public void uploadMultipartPart(String uploadId, int partNumber, byte[] data) {
		if (partNumber < 1 || partNumber > 10000) {
			throw new IllegalArgumentException("partNumber 必須介於 1 到 10000");
		}
		if (data == null || data.length == 0) {
			throw new IllegalArgumentException("分片內容不可為空");
		}
		PendingUpload pending = pendingUploadRegistry.get(uploadId);
		String eTag = s3Util.uploadPart(s3Properties.getBucketName(), pending.getObjectKey(), uploadId, partNumber, data);
		pending.getParts().add(CompletedPart.builder().partNumber(partNumber).eTag(eTag).build());
		pending.getTotalBytes().addAndGet(data.length);

		log.info("S3 分片上傳成功: key={}, uploadId={}, partNumber={}, size={}", pending.getObjectKey(), uploadId, partNumber, data.length);
	}

	// 第 3 步：依 partNumber 排序後，通知 S3 組裝檔案。
	public String completeMultipartUpload(String uploadId) {
		PendingUpload pendingUpload = pendingUploadRegistry.get(uploadId);

		if (pendingUpload.getParts().isEmpty()) {
			throw new IllegalStateException("目前沒有已上傳的分片");
		}

		List<CompletedPart> completedParts = pendingUpload.getParts()
		        .stream()
		        .sorted(Comparator.comparingInt(CompletedPart::partNumber))
		        .collect(Collectors.toList());

		try {
			s3Util.completeMultipartUpload(s3Properties.getBucketName(), pendingUpload.getObjectKey(), uploadId, completedParts);
			long totalSize = pendingUpload.getTotalBytes().get();
			log.info("S3 分片上傳完成: key={}, uploadId={}, partCount={}, size={}", pendingUpload.getObjectKey(), uploadId, completedParts.size(), totalSize);
			return pendingUpload.getObjectKey();

		} catch (RuntimeException e) {
			log.error("S3 分片組裝失敗，嘗試中止上傳: key={}, uploadId={}", pendingUpload.getObjectKey(), uploadId, e);
			s3Util.abortMultipartUpload(s3Properties.getBucketName(), pendingUpload.getObjectKey(), uploadId);
			throw e;

		} finally {
			pendingUploadRegistry.remove(uploadId);
		}
	}

	// 中止尚未完成的 multipart upload。
	public void abortMultipartUpload(String uploadId) {
		PendingUpload pendingUpload = pendingUploadRegistry.get(uploadId);
		s3Util.abortMultipartUpload(s3Properties.getBucketName(), pendingUpload.getObjectKey(), uploadId);
		pendingUploadRegistry.remove(uploadId);
		log.info("S3 分片上傳已中止: key={}, uploadId={}", pendingUpload.getObjectKey(), uploadId);
	}
	
}
