package com.holidaydessert.constant;

import java.io.File;
import java.nio.file.Paths;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuildPath {

    // =============================================
    // 設定檔注入（application.yml）
    // Spring 無法直接注入 static 欄位，先注入實例欄位，再於 @PostConstruct 搬到 static 欄位供外部使用
    // =============================================
    @Value("${admin.upload.file.path}")
    private String adminUploadFilePath;

    @Value("${web.path}")
    private String webPath;

    // 上傳檔案根目錄（admin.upload.file.path），僅供 buildUploadPath 使用
    private static String ADMIN_UPLOAD_FILE_PATH;
    // 網站完整網址（web.path），例如 http://localhost:8080/
    public static String WEB_PATH;

    @PostConstruct
    public void init() {
        ADMIN_UPLOAD_FILE_PATH = this.adminUploadFilePath;
        WEB_PATH = this.webPath;
    }

    // =============================================
    // Context Path（需與 application.yml 的 server.servlet.context-path 一致）
    // =============================================
    private static final String CONTEXT    = "holidayDessert";
    public static final String CONTEXT_PATH = "/" + CONTEXT;

    // =============================================
    // Folder
    // =============================================
    public static final String IMAGES     = "images";
    // Table（同時作為上傳子資料夾名稱）
    public static final String COUPON     = "coupon";
    public static final String EMPLOYEE   = "employee";
    public static final String BANNER     = "banner";
    public static final String PRODUCTPIC = "productPic";

    // =============================================
    // Upload Sub Paths（搭配 buildUploadPath 使用，例如 images/coupon）
    // =============================================
    public static final String COUPON_IMAGE_FOLDER     = imageFolder(COUPON);
    public static final String EMPLOYEE_IMAGE_FOLDER   = imageFolder(EMPLOYEE);
    public static final String BANNER_IMAGE_FOLDER     = imageFolder(BANNER);
    public static final String PRODUCTPIC_IMAGE_FOLDER = imageFolder(PRODUCTPIC);

    // =============================================
    // Web Access Paths（存入 DB 的圖片網址前綴，例如 holidayDessert/admin/upload/images/coupon/）
    // =============================================
    private static final String WEB_BASE = CONTEXT + "/admin/upload/";

    public static final String COUPON_WEB_PATH     = imageWebPath(COUPON);
    public static final String EMPLOYEE_WEB_PATH   = imageWebPath(EMPLOYEE);
    public static final String BANNER_WEB_PATH     = imageWebPath(BANNER);
    public static final String PRODUCTPIC_WEB_PATH = imageWebPath(PRODUCTPIC);

    // =============================================
    // Admin Redirect Paths（後台導向路徑）
    // 搭配 model.addAttribute("PATH", ...) 使用，操作完成後前端依此導回指定頁面
    // =============================================
    private static final String ADMIN_BASE = CONTEXT_PATH + "/admin/";
    private static final String LIST       = "list";

    // 後台模組（對應 @RequestMapping("/admin/{module}")），COUPON、EMPLOYEE 沿用上方 Table 常數
    public static final String COMPANY   = "company";
    public static final String PRODUCT   = "product";
    public static final String NEWS      = "news";
    public static final String CALENDAR  = "calendar";
    public static final String PROMOTION = "promotion";
    public static final String TICKET    = "ticket";

    // 列表頁
    public static final String ADMIN_COMPANY_LIST   = adminPath(COMPANY,   LIST);
    public static final String ADMIN_PRODUCT_LIST   = adminPath(PRODUCT,   LIST);
    public static final String ADMIN_NEWS_LIST      = adminPath(NEWS,      LIST);
    public static final String ADMIN_CALENDAR_LIST  = adminPath(CALENDAR,  LIST);
    public static final String ADMIN_COUPON_LIST    = adminPath(COUPON,    LIST);
    public static final String ADMIN_EMPLOYEE_LIST  = adminPath(EMPLOYEE,  LIST);
    public static final String ADMIN_PROMOTION_LIST = adminPath(PROMOTION, LIST);
    public static final String ADMIN_TICKET_LIST    = adminPath(TICKET,    LIST);

    // 其他頁面
    public static final String ADMIN_TICKET_ADD = adminPath(TICKET, "addTicket");

    // 組合後台頁面路徑
    // 例如 adminPath(COMPANY, "list") → /holidayDessert/admin/company/list
    public static String adminPath(String module, String page) {
        return ADMIN_BASE + module + "/" + page;
    }

    // 商品圖片編輯頁：/holidayDessert/admin/product/editProductPic?pdId={pdId}
    public static String adminProductPicPath(Object pdId) {
        return adminPath(PRODUCT, "editProductPic") + "?pdId=" + pdId;
    }

    // 輪播圖編輯頁：/holidayDessert/admin/news/editBanner?newsId={newsId}
    public static String adminBannerPath(Object newsId) {
        return adminPath(NEWS, "editBanner") + "?newsId=" + newsId;
    }

    // 活動修改頁：/holidayDessert/admin/ticket/updateTicket?ticketId={ticketId}
    public static String adminTicketUpdatePath(Object ticketId) {
        return adminPath(TICKET, "updateTicket") + "?ticketId=" + ticketId;
    }

    // =============================================
    // Front Redirect Paths（前台導向路徑）
    // 不含 .html 的路徑供 SecurityConfig.resolveRedirectPath 依來源決定加 .html（8080）或 Vue 網址（8081）
    // =============================================
    private static final String HTML = ".html";

    // 前台模組
    public static final String MEMBER = "member";

    // 首頁
    public static final String FRONT_INDEX      = CONTEXT_PATH + "/index";
    public static final String FRONT_INDEX_HTML = FRONT_INDEX + HTML;

    // 會員
    public static final String FRONT_MEMBER_UPDATE                    = frontPath(MEMBER, "update");
    public static final String FRONT_MEMBER_VERIFICATION_HTML         = frontPath(MEMBER, "verification") + HTML;
    public static final String FRONT_MEMBER_VERIFICATION_SUCCESS_HTML = frontPath(MEMBER, "verificationSuccess") + HTML;

    // 組合前台頁面路徑
    // 例如 frontPath(MEMBER, "update") → /holidayDessert/member/update
    public static String frontPath(String module, String page) {
        return CONTEXT_PATH + "/" + module + "/" + page;
    }

    // =============================================
    // 工具方法
    // =============================================
    // 組合完整上傳路徑（Paths 自動處理 OS 分隔符，不需要手動判斷 Windows/Linux/Mac）
    // 例如 buildUploadPath(COUPON_IMAGE_FOLDER) → {admin.upload.file.path}/images/coupon/
    public static String buildUploadPath(String subFolder) {
        return Paths.get(ADMIN_UPLOAD_FILE_PATH, subFolder).toString() + File.separator;
    }

    // 上傳子資料夾：images{分隔符}{table}
    private static String imageFolder(String table) {
        return IMAGES + File.separator + table;
    }

    // 圖片網址前綴：holidayDessert/admin/upload/images/{table}/
    private static String imageWebPath(String table) {
        return WEB_BASE + IMAGES + "/" + table + "/";
    }

}
