package com.holidaydessert.utils;

import org.springframework.data.domain.Sort;

import java.util.Map;

public final class SortUtil {

	private SortUtil() {
	}

	/**
	 * 建立排序條件
	 *
	 * @param frontendSortField 前端傳入的欄位名稱
	 * @param order             ASC / DESC
	 * @param fieldMapping      前端欄位與 Entity 欄位的對應
	 * @param defaultField      預設 Entity 排序欄位
	 * @param defaultDirection  預設排序方向
	 * @return Sort
	 */
	public static Sort buildSort(String frontendSortField, String order, Map<String, String> fieldMapping, String defaultField, Sort.Direction defaultDirection) {
		if (frontendSortField == null || frontendSortField.trim().isEmpty()) {
			return Sort.by(defaultDirection, defaultField);
		}
		String entitySortField = fieldMapping.get(frontendSortField);
		// 前端傳入不在白名單內時， 不直接使用前端值，而是使用預設排序欄位
		if (entitySortField == null || entitySortField.trim().isEmpty()) {
			return Sort.by(defaultDirection, defaultField);
		}

		Sort.Direction direction = "ASC".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
		return Sort.by(direction, entitySortField);
	}
	
}