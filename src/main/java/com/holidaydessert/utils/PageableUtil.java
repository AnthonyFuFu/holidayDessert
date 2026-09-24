package com.holidaydessert.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分頁工具類，統一處理分頁參數轉換與結果封裝
 */
public final class PageableUtil {

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final String DEFAULT_ORDER = "DESC";

	private PageableUtil() {
	}

	/**
	 * 從 Map 參數建立 Pageable（含排序）
	 *
	 * @param params       請求參數 Map（需含 rows / pageIndex / sort / order）
	 * @param fieldMapping 前端欄位與 Entity 欄位的對應（對應 SortFieldsConstant 的 Map）
	 * @param defaultField 預設排序欄位
	 * @param defaultDir   預設排序方向
	 * @return Pageable
	 */
	public static Pageable buildPageable(Map<String, Object> params, Map<String, String> fieldMapping, String defaultField, Sort.Direction defaultDir) {
		
		int pageSize = parseIntParam(params, "rows", DEFAULT_PAGE_SIZE);
		int offset = parseIntParam(params, "pageIndex", 0);
		int pageNumber = pageSize > 0 ? offset / pageSize : 0;
		String sortParam = getStringParam(params, "sort", "");
		String orderParam = getStringParam(params, "order", DEFAULT_ORDER);

		Sort sort = SortUtil.buildSort(sortParam, orderParam, fieldMapping, defaultField, defaultDir);

		return PageRequest.of(pageNumber, pageSize, sort);
	}

	/**
	 * 從 Map 參數建立 Pageable（不帶排序）
	 *
	 * @param params 請求參數 Map（需含 rows / pageIndex）
	 * @return Pageable
	 */
	public static Pageable buildPageable(Map<String, Object> params) {
		int pageSize = parseIntParam(params, "rows", DEFAULT_PAGE_SIZE);
		int offset = parseIntParam(params, "pageIndex", 0);
		int pageNumber = pageSize > 0 ? offset / pageSize : 0;

		return PageRequest.of(pageNumber, pageSize);
	}

	/**
	 * 將 Page 結果封裝成統一回傳格式
	 *
	 * @param page    Spring Data 的 Page 物件（用於取得 totalElements）
	 * @param dtoList 已轉換完成的 DTO List
	 * @return Map 含 list 與 totalRecords
	 */
	public static <T> Map<String, Object> buildResult(Page<?> page, List<T> dtoList) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("list", dtoList);
		resultMap.put("totalRecords", page.getTotalElements());
		return resultMap;
	}

	// 從 params 取得字串參數，若不存在則回傳預設值
	public static String getStringParam(Map<String, Object> params, String key, String defaultValue) {
		return params.get(key) != null ? params.get(key).toString() : defaultValue;
	}
	
	private static int parseIntParam(Map<String, Object> params, String key, int defaultValue) {
		try {
			return params.get(key) != null ? Integer.parseInt(params.get(key).toString()) : defaultValue;
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
