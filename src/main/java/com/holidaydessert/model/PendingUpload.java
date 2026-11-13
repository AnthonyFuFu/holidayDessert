package com.holidaydessert.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.CompletedPart;

// PCBFile 分片上傳(init/part/complete/abort)過程中的中繼資料
@Getter
@RequiredArgsConstructor
public class PendingUpload {
    private final String objectKey;
    private final List<CompletedPart> parts = new CopyOnWriteArrayList<>();
    private final AtomicLong totalBytes = new AtomicLong();
}
