package com.holidaydessert.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3MultipartForwardService {

	private static final int CHUNK_SIZE = 20 * 1024 * 1024;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	//將收到的檔案切片後，轉送至目標服務。
	public String forwardFile(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("上傳檔案不可為空");
		}

		String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename());
		String targetBaseUrl = "http://localhost:8081/api/s3"; // 目標服務的 URL
		// 使用唯一 key，避免相同檔名互相覆蓋
		String key = "uploads/" + UUID.randomUUID() + "/" + originalFilename;

		String uploadId = null;

		try {
			// 第 1 步：初始化目標服務的 multipart upload
			String initUrl = UriComponentsBuilder.fromUriString(targetBaseUrl + "/multipart/init")
					.queryParam("key", key)
					.queryParam("contentType", file.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType())
					.build().encode().toUriString();

			String initBody = postForBody(initUrl, null);
			JsonNode initJson = parseAndCheckSuccess(initBody, "初始化分片上傳");

			JsonNode uploadIdNode = initJson.path("result").path("uploadId");
			if (uploadIdNode.isMissingNode() || uploadIdNode.asText().isBlank()) {
				throw new IllegalStateException("目標服務初始化成功，但沒有回傳 uploadId：" + initBody);
			}

			uploadId = uploadIdNode.asText();

			// 第 2 步：逐片傳送；記憶體中只保留目前這一片
			int partNumber = 1;

			try (InputStream inputStream = file.getInputStream()) {
				byte[] chunk;

				while ((chunk = inputStream.readNBytes(CHUNK_SIZE)).length > 0) {
					String partUrl = UriComponentsBuilder.fromUriString(targetBaseUrl + "/multipart/part")
							.queryParam("uploadId", uploadId).queryParam("partNumber", partNumber).build().encode()
							.toUriString();

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

					HttpEntity<byte[]> request = new HttpEntity<>(chunk, headers);

					ResponseEntity<String> response = restTemplate.exchange(partUrl, HttpMethod.POST, request, String.class);

					checkSuccess(response.getBody(), "上傳分片 " + partNumber);

					log.info("檔案分片轉送成功: key={}, partNumber={}, size={}", key, partNumber, chunk.length);

					partNumber++;
				}
			}

			// 第 3 步：請目標服務組裝分片
			String completeUrl = UriComponentsBuilder.fromUriString(targetBaseUrl + "/multipart/complete").queryParam("uploadId", uploadId).build().encode().toUriString();

			String completeBody = postForBody(completeUrl, null);
			JsonNode completeJson = parseAndCheckSuccess(completeBody, "完成分片上傳");

			JsonNode keyNode = completeJson.path("result").path("key");

			log.info("檔案分片轉送完成: key={}, uploadId={}", key, uploadId);

			// 目標服務有回傳 key 時使用目標服務的結果，否則回傳初始化時使用的 key
			return keyNode.isMissingNode() || keyNode.asText().isBlank() ? key : keyNode.asText();

		} catch (Exception e) {
			if (uploadId != null) {
				abortQuietly(uploadId);
			}

			log.error("檔案分片轉送失敗: key={}, uploadId={}", key, uploadId, e);

			if (e instanceof IOException) {
				throw (IOException) e;
			}

			throw new IllegalStateException("檔案分片轉送失敗：" + e.getMessage(), e);
		}
	}

	private String postForBody(String url, HttpEntity<?> request) {
		ResponseEntity<String> response = request == null ? restTemplate.postForEntity(url, null, String.class) : restTemplate.exchange(url, HttpMethod.POST, request, String.class);

		return response.getBody();
	}

	private JsonNode parseAndCheckSuccess(String body, String action) {
		checkSuccess(body, action);

		try {
			return objectMapper.readTree(body);
		} catch (IOException e) {
			throw new IllegalStateException(action + "回應不是有效 JSON：" + body, e);
		}
	}

	private void checkSuccess(String body, String action) {
		if (body == null || body.isBlank()) {
			throw new IllegalStateException(action + "失敗：目標服務沒有回傳內容");
		}

		try {
			JsonNode json = objectMapper.readTree(body);
			int code = json.path("code").asInt(-1);

			if (code != 200) {
				throw new IllegalStateException(action + "失敗，回應：" + body);
			}
		} catch (IOException e) {
			throw new IllegalStateException(action + "失敗，回應不是有效 JSON：" + body, e);
		}
	}

	private void abortQuietly(String uploadId) {
		try {
			String targetBaseUrl = "http://localhost:8081/api/s3"; // 目標服務的 URL
			String abortUrl = UriComponentsBuilder.fromUriString(targetBaseUrl + "/multipart/abort").queryParam("uploadId", uploadId).build().encode().toUriString();
			postForBody(abortUrl, null);

		} catch (Exception abortException) {
			log.warn("嘗試中止目標服務的 multipart upload 失敗: uploadId={}", uploadId, abortException);
		}
	}
}
