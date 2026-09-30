package com.holidaydessert.utils;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
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

	/**
	 * 從 DataTables（serverSide）參數建立排序條件，支援多欄排序（Shift + 點擊）
	 * DataTables 會送出：
	 *   order[i][column] = 欄位索引
	 *   order[i][dir]    = asc / desc
	 *   columns[n][data] = 該欄位的 data 名稱（對應 fieldMapping 的 key）
	 *
	 * @param params           DataTables 請求參數 Map
	 * @param fieldMapping     DataTables 欄位 data 名稱與 DB / Entity 欄位的對應（白名單）
	 * @param defaultField     預設排序欄位
	 * @param defaultDirection 預設排序方向
	 * @return Sort
	 */
	public static Sort buildDataTableSort(Map<String, ?> params, Map<String, String> fieldMapping, String defaultField, Sort.Direction defaultDirection) {
		List<Sort.Order> orders = new ArrayList<>();
		for (int i = 0; params.get("order[" + i + "][column]") != null; i++) {
			String columnIndex = params.get("order[" + i + "][column]").toString();
			Object columnData = params.get("columns[" + columnIndex + "][data]");
			Object orderable = params.get("columns[" + columnIndex + "][orderable]");
			// 前端設定 orderable: false 的欄位不排序
			if (columnData == null || "false".equalsIgnoreCase(String.valueOf(orderable))) {
				continue;
			}
			String entitySortField = fieldMapping.get(columnData.toString());
			// 前端傳入不在白名單內時，略過該欄位
			if (entitySortField == null || entitySortField.trim().isEmpty()) {
				continue;
			}
			Object dir = params.get("order[" + i + "][dir]");
			Sort.Direction direction = "asc".equalsIgnoreCase(String.valueOf(dir)) ? Sort.Direction.ASC : Sort.Direction.DESC;
			orders.add(new Sort.Order(direction, entitySortField));
		}
		if (orders.isEmpty()) {
			return Sort.by(defaultDirection, defaultField);
		}
		return Sort.by(orders);
	}

}
