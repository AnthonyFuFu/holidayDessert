package com.holidaydessert.utils;

import java.util.function.Supplier;

import org.springframework.ui.Model;

import lombok.extern.slf4j.Slf4j;

// Controller 共用工具：把重複的 try/catch 收在這裡，Controller 只描述「要做什麼」
// 用法：import static com.holidaydessert.utils.ControllerUtil.*;
@Slf4j
public final class ControllerUtil {

	// 新增 / 修改完成後的轉址頁：有 MESSAGE 就先 alert，再跳轉到 PATH
	public static final String TO_PATH = "admin/toPath";

	private static final String ADD_SUCCESS    = "資料新增成功";
	private static final String ADD_FAIL       = "新增失敗，請重新操作";
	private static final String UPDATE_FAIL    = "修改失敗，請重新操作";

	private ControllerUtil() {
	}

	// 可丟出例外的動作（新增、修改、上傳檔案等）
	@FunctionalInterface
	public interface Action {
		void run() throws Exception;
	}

	// 查詢失敗時記錄 log 並回傳預設值，單一區塊失敗不影響整個頁面
	// 只用在「沒有也不影響操作」的資料，例如下拉選單、儀表板區塊
	// 例如 model.addAttribute("promotionList", safe("promotionList", promotionService::getList, Collections.emptyList()));
	public static <T> T safe(String name, Supplier<T> supplier, T fallback) {
		try {
			return supplier.get();
		} catch (Exception e) {
			log.error("[Safe] {} 查詢失敗", name, e);
			return fallback;
		}
	}

	// 新增：成功提示「資料新增成功」，失敗提示「新增失敗」，最後都導回 path
	// 例如 return addSubmit(model, ADMIN_NEWS_LIST, () -> newsService.add(news));
	public static String addSubmit(Model model, String path, Action action) {
		return submit(model, path, ADD_SUCCESS, ADD_FAIL, action);
	}

	// 修改：成功不另外提示（前端送出前已顯示「修改完成」），失敗提示「修改失敗」，最後都導回 path
	// 例如 return updateSubmit(model, ADMIN_NEWS_LIST, () -> newsService.update(news));
	public static String updateSubmit(Model model, String path, Action action) {
		return submit(model, path, null, UPDATE_FAIL, action);
	}

	// 執行動作並設定提示訊息與導向頁面；successMessage 為 null 代表成功時不提示
	public static String submit(Model model, String path, String successMessage, String failMessage, Action action) {
		try {
			action.run();
			if (successMessage != null) {
				model.addAttribute("MESSAGE", successMessage);
			}
		} catch (Exception e) {
			log.error("[Submit] {} path={}", failMessage, path, e);
			model.addAttribute("MESSAGE", failMessage);
		}
		model.addAttribute("PATH", path);
		return TO_PATH;
	}

}
