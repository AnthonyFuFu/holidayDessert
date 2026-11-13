package com.holidaydessert.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.holidaydessert.model.PendingUpload;

// 管理 PCBFile 分片上傳過程中的中繼資料；僅存於單一實例記憶體，多副本部署需改用 Redis/DB 等共享儲存
@Component
public class PendingUploadRegistry {

	private final Map<String, PendingUpload> pendingUploads = new ConcurrentHashMap<>();

	public void register(String uploadId, PendingUpload pendingUpload) {
		pendingUploads.put(uploadId, pendingUpload);
	}

	// 找不到對應的上傳中繼資料(含逾時被清除)時丟出例外，呼叫端無須再自行判斷 null
	public PendingUpload get(String uploadId) {
		PendingUpload pending = pendingUploads.get(uploadId);
		if (pending == null) {
			throw new IllegalStateException("找不到對應的分片上傳或已逾時，uploadId=" + uploadId);
		}
		return pending;
	}

	public PendingUpload remove(String uploadId) {
		return pendingUploads.remove(uploadId);
	}
}
