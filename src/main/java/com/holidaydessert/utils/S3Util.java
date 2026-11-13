package com.holidaydessert.utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@Service
public class S3Util {

	private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	private static final Duration PRESIGNED_URL_DURATION = Duration.ofMinutes(10);
	// 每個分片大小；過程中同一時間只會有一個分片的內容留在記憶體中，不受檔案總大小影響
	private static final long MULTIPART_CHUNK_SIZE = 20L * 1024 * 1024;
	
	@Autowired
	private S3Client s3Client;

	@Autowired
	private S3Presigner s3Presigner;

	// 取得 S3 預簽名下載網址
	public String getPresignedGetObject(String bucketName, String objectKey) {
		GetObjectRequest getObjectRequest = buildGetObjectRequest(bucketName, objectKey);
		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
				.signatureDuration(PRESIGNED_URL_DURATION)
				.getObjectRequest(getObjectRequest)
				.build();
		return s3Presigner.presignGetObject(presignRequest).url().toString();
	}

	// 取得 S3 檔案 metadata
	public HeadObjectResponse getStatObject(String bucketName, String objectKey) {
		HeadObjectRequest request = HeadObjectRequest.builder().bucket(bucketName).key(objectKey).build();
		return s3Client.headObject(request);
	}

	// 取得 S3 InputStream
	public ResponseInputStream<GetObjectResponse> getObjectInputStream(String bucketName, String objectKey) {
		GetObjectRequest request = buildGetObjectRequest(bucketName, objectKey);
		return s3Client.getObject(request);
	}

	// 使用 InputStream 上傳檔案，適合大型檔案
	public void putObject(String bucketName, String objectKey, InputStream inputStream, long fileSize, String contentType) {
		PutObjectRequest request = buildPutObjectRequest(bucketName, objectKey, contentType);
		s3Client.putObject(request, RequestBody.fromInputStream(inputStream, fileSize));
	}

	// 使用 MultipartFile 上傳檔案；檔案超過門檻時對呼叫端透明地改走 Multipart Upload，避免單次 PutObject 的 5GB 上限與長時間單一請求風險
	public void putObject(String bucketName, String objectKey, MultipartFile file) throws IOException {
		try (InputStream inputStream = file.getInputStream()) {
			if (file.getSize() > MULTIPART_CHUNK_SIZE) {
				putObjectMultipart(bucketName, objectKey, inputStream, file.getSize(), file.getContentType());
			} else {
				putObject(bucketName, objectKey, inputStream, file.getSize(), file.getContentType());
			}
		}
	}

	// 使用 byte[] 上傳檔案，適合小型檔案
	public void putObject(String bucketName, String objectKey, byte[] fileBytes, String contentType) {
		PutObjectRequest request = buildPutObjectRequest(bucketName, objectKey, contentType);
		s3Client.putObject(request, RequestBody.fromBytes(fileBytes));
	}

	// 刪除 S3 檔案
	public void deleteObject(String bucketName, String objectKey) {
		try {
			DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build();
			s3Client.deleteObject(request);
			log.info("S3 檔案刪除成功: {}", objectKey);
		} catch (Exception e) {
			log.warn("S3 檔案刪除失敗，若為同檔名覆蓋情境則不影響功能: objectKey={}, message={}", objectKey, e.getMessage());
		}
	}

	// 複製 S3 檔案
	public void copyObject(String sourceBucket, String sourceKey, String destinationBucket, String destinationKey) {
		CopyObjectRequest request = CopyObjectRequest.builder()
				.sourceBucket(sourceBucket)
				.sourceKey(sourceKey)
				.destinationBucket(destinationBucket)
				.destinationKey(destinationKey)
				.build();
		s3Client.copyObject(request);
	}

	// 建立 Multipart Upload，取得 uploadId (此步驟不會影響 objectKey 現有的物件內容)
	public String createMultipartUpload(String bucketName, String objectKey, String contentType) {
		CreateMultipartUploadRequest request = CreateMultipartUploadRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.contentType(normalizeContentType(contentType))
				.build();
		return s3Client.createMultipartUpload(request).uploadId();
	}

	// 上傳單一分片；供跨服務分片轉送(如 APIM -> PCBA)使用，呼叫端自行依固定大小切片
	public String uploadPart(String bucketName, String objectKey, String uploadId, int partNumber, byte[] data) {
		UploadPartRequest request = UploadPartRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.uploadId(uploadId)
				.partNumber(partNumber)
				.build();
		return s3Client.uploadPart(request, RequestBody.fromBytes(data)).eTag();
	}

	// 邊讀邊傳：從 InputStream 依序切出固定大小的分片，逐片呼叫 S3 uploadPart，記憶體中僅需保留單一分片的內容
	private void putObjectMultipart(String bucketName, String objectKey, InputStream inputStream, long fileSize, String contentType) throws IOException {
		String uploadId = createMultipartUpload(bucketName, objectKey, contentType);
		List<CompletedPart> completedParts = new ArrayList<>();
		try {
			long remaining = fileSize;
			int partNumber = 1;
			while (remaining > 0) {
				int chunkSize = (int) Math.min(MULTIPART_CHUNK_SIZE, remaining);
				byte[] buffer = new byte[chunkSize];
				int bytesRead = inputStream.readNBytes(buffer, 0, chunkSize);
				if (bytesRead <= 0) {
					break;
				}
				UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
						.bucket(bucketName)
						.key(objectKey)
						.uploadId(uploadId)
						.partNumber(partNumber)
						.build();
				UploadPartResponse uploadPartResponse = s3Client.uploadPart(uploadPartRequest,
						RequestBody.fromBytes(bytesRead == chunkSize ? buffer : Arrays.copyOf(buffer, bytesRead)));
				completedParts.add(CompletedPart.builder().partNumber(partNumber).eTag(uploadPartResponse.eTag()).build());
				remaining -= bytesRead;
				partNumber++;
			}
			completeMultipartUpload(bucketName, objectKey, uploadId, completedParts);
			log.info("S3 Multipart Upload 完成: objectKey={}, uploadId={}, partCount={}", objectKey, uploadId, completedParts.size());
		} catch (Exception e) {
			abortMultipartUpload(bucketName, objectKey, uploadId);
			throw e;
		}
	}

	// 組裝所有分片，完成 Multipart Upload；此時才會真正覆蓋 objectKey 原有的物件內容
	public void completeMultipartUpload(String bucketName, String objectKey, String uploadId, List<CompletedPart> parts) {
		CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder().parts(parts).build();
		CompleteMultipartUploadRequest request = CompleteMultipartUploadRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.uploadId(uploadId)
				.multipartUpload(completedMultipartUpload)
				.build();
		s3Client.completeMultipartUpload(request);
	}

	// 中止 Multipart Upload，釋放已上傳但尚未組裝的分片 (失敗或用戶端取消時呼叫)
	public void abortMultipartUpload(String bucketName, String objectKey, String uploadId) {
		try {
			AbortMultipartUploadRequest request = AbortMultipartUploadRequest.builder()
					.bucket(bucketName)
					.key(objectKey)
					.uploadId(uploadId)
					.build();
			s3Client.abortMultipartUpload(request);
		} catch (Exception e) {
			log.warn("S3 Multipart Upload 中止失敗: objectKey={}, uploadId={}, message={}", objectKey, uploadId, e.getMessage());
		}
	}

	// 建立 GetObjectRequest
	private static GetObjectRequest buildGetObjectRequest(String bucketName, String objectKey) {
		return GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
	}

	// 建立 PutObjectRequest
	private static PutObjectRequest buildPutObjectRequest(String bucketName, String objectKey, String contentType) {
		return PutObjectRequest.builder()
				.bucket(bucketName)
				.key(objectKey)
				.contentType(normalizeContentType(contentType))
				.build();
	}

	// 避免 contentType 為 null
	private static String normalizeContentType(String contentType) {
		return contentType == null || contentType.isBlank() ? DEFAULT_CONTENT_TYPE : contentType;
	}
}
