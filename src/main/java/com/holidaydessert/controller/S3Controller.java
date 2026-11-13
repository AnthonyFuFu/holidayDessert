package com.holidaydessert.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.service.S3MultipartForwardService;
import com.holidaydessert.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/s3")
@Tag(name = "S3 檔案管理")
public class S3Controller {

	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private S3MultipartForwardService s3MultipartForwardService;
	
	// 第 1 步：建立 multipart upload，回傳 uploadId
	@PostMapping("/multipart/init")
	@Operation(summary = "初始化 S3 分片上傳")
	public ResponseEntity<ApiReturnObject> initMultipartUpload(@RequestParam String key, @RequestParam(required = false) String contentType) {

		String uploadId = s3Service.initMultipartUpload(key, contentType);
		ApiReturnObject result = ApiReturnObject.success("分片上傳初始化成功", Map.of("uploadId", uploadId, "key", key));

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	// 第 2 步：上傳單一分片，Request Body 是該分片的原始 bytes
	@PostMapping(value = "/multipart/part", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@Operation(summary = "上傳 S3 分片")
	public ResponseEntity<ApiReturnObject> uploadMultipartPart(@RequestParam String uploadId, @RequestParam int partNumber, @RequestBody byte[] data) {

		s3Service.uploadMultipartPart(uploadId, partNumber, data);
		ApiReturnObject result = ApiReturnObject.success("分片上傳成功", Map.of("partNumber", partNumber));

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	// 第 3 步：完成 multipart upload
	@PostMapping("/multipart/complete")
	@Operation(summary = "完成 S3 分片上傳")
	public ResponseEntity<ApiReturnObject> completeMultipartUpload(@RequestParam String uploadId) {

		String key = s3Service.completeMultipartUpload(uploadId);
		ApiReturnObject result = ApiReturnObject.success("檔案上傳成功", Map.of("key", key));

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	// 上傳失敗時中止 multipart upload
	@PostMapping("/multipart/abort")
	@Operation(summary = "中止 S3 分片上傳")
	public ResponseEntity<ApiReturnObject> abortMultipartUpload(@RequestParam String uploadId) {

		s3Service.abortMultipartUpload(uploadId);
		ApiReturnObject result = ApiReturnObject.success("已中止分片上傳", Map.of("uploadId", uploadId));

		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PostMapping(value = "/multipart/forward", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "將檔案切片轉送至目標服務")
	public ResponseEntity<ApiReturnObject> forwardFile(@RequestParam("file") MultipartFile file) {

		try {
			String key = s3MultipartForwardService.forwardFile(file);
			return ResponseEntity.ok(ApiReturnObject.success("檔案分片轉送成功", Map.of("key", key)));

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiReturnObject.badRequest(e.getMessage()));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiReturnObject.serverError(e.getMessage()));
		}
	}
	
}
