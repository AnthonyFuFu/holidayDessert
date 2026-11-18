package com.holidaydessert.constant;

import java.io.File;
import java.nio.file.Paths;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuildPath {

    // =============================================
    // 設定檔注入
    // Spring 無法直接注入 static 欄位，先注入實例欄位，再於 @PostConstruct 搬到 static 欄位
    // =============================================
    @Value("${admin.upload.file.path}")
    private String adminUploadFilePath;

    @Value("${web.path}")
    private String webPath;

    private static String ADMIN_UPLOAD_FILE_PATH;
    public static String WEB_PATH;

    @PostConstruct
    public void init() {
        ADMIN_UPLOAD_FILE_PATH = this.adminUploadFilePath;
        WEB_PATH = this.webPath;
    }

    // =============================================
    // 基礎片段
    // =============================================
    // 需與 application.yml 的 server.servlet.context-path 一致
    private static final String CONTEXT     = "holidayDessert";
    public static final String CONTEXT_PATH = "/" + CONTEXT;

    private static final String ADMIN_BASE = CONTEXT_PATH + "/admin/";
    private static final String WEB_BASE   = CONTEXT + "/admin/upload/";
    private static final String HTML       = ".html";
    private static final String LIST       = "list";

    // =============================================
    // 名稱
    // =============================================
    public static final String IMAGES = "images";

    // 資料表（同時作為上傳子資料夾、後台模組名稱）
    public static final String COUPON     = "coupon";
    public static final String EMPLOYEE   = "employee";
    public static final String BANNER     = "banner";
    public static final String PRODUCTPIC = "productPic";

    // 後台模組
    public static final String COMPANY   = "company";
    public static final String PRODUCT   = "product";
    public static final String NEWS      = "news";
    public static final String CALENDAR  = "calendar";
    public static final String PROMOTION = "promotion";
    public static final String TICKET    = "ticket";

    // 前台模組
    public static final String MEMBER = "member";

    // =============================================
    // 上傳檔案路徑
    // =============================================
    // 硬碟子資料夾，搭配 buildUploadPath 使用
    public static final String COUPON_IMAGE_FOLDER     = imageFolder(COUPON);
    public static final String EMPLOYEE_IMAGE_FOLDER   = imageFolder(EMPLOYEE);
    public static final String BANNER_IMAGE_FOLDER     = imageFolder(BANNER);
    public static final String PRODUCTPIC_IMAGE_FOLDER = imageFolder(PRODUCTPIC);

    // 網址前綴，存入 DB 供頁面顯示
    public static final String COUPON_WEB_PATH     = imageWebPath(COUPON);
    public static final String EMPLOYEE_WEB_PATH   = imageWebPath(EMPLOYEE);
    public static final String BANNER_WEB_PATH     = imageWebPath(BANNER);
    public static final String PRODUCTPIC_WEB_PATH = imageWebPath(PRODUCTPIC);

    // =============================================
    // 後台導向路徑（model.addAttribute("PATH", ...)）
    // 需要帶參數的頁面請用下方 adminXxxPath() 方法
    // =============================================
    public static final String ADMIN_CALENDAR_LIST  = adminPath(CALENDAR,  LIST);
    public static final String ADMIN_COMPANY_LIST   = adminPath(COMPANY,   LIST);
    public static final String ADMIN_COUPON_LIST    = adminPath(COUPON,    LIST);
    public static final String ADMIN_EMPLOYEE_LIST  = adminPath(EMPLOYEE,  LIST);
    public static final String ADMIN_NEWS_LIST      = adminPath(NEWS,      LIST);
    public static final String ADMIN_PRODUCT_LIST   = adminPath(PRODUCT,   LIST);
    public static final String ADMIN_PROMOTION_LIST = adminPath(PROMOTION, LIST);
    public static final String ADMIN_TICKET_LIST    = adminPath(TICKET,    LIST);

    public static final String ADMIN_TICKET_ADD = adminPath(TICKET, "addTicket");

    // =============================================
    // 前台導向路徑
    // FRONT_INDEX 不含 .html：由 SecurityConfig.resolveRedirectPath 依來源決定加 .html（8080）或導向 Vue（8081）
    // =============================================
    public static final String FRONT_INDEX      = CONTEXT_PATH + "/index";
    public static final String FRONT_INDEX_HTML = FRONT_INDEX + HTML;

    public static final String FRONT_MEMBER_UPDATE                    = frontPath(MEMBER, "update");
    public static final String FRONT_MEMBER_VERIFICATION_HTML         = frontPath(MEMBER, "verification") + HTML;
    public static final String FRONT_MEMBER_VERIFICATION_SUCCESS_HTML = frontPath(MEMBER, "verificationSuccess") + HTML;

    // =============================================
    // 組合方法
    // =============================================
    // adminPath(COMPANY, "list") → /holidayDessert/admin/company/list
    public static String adminPath(String module, String page) {
        return ADMIN_BASE + module + "/" + page;
    }

    public static String adminProductPicPath(Object pdId) {
        return adminPath(PRODUCT, "editProductPic") + "?pdId=" + pdId;
    }

    public static String adminBannerPath(Object newsId) {
        return adminPath(NEWS, "editBanner") + "?newsId=" + newsId;
    }

    public static String adminTicketUpdatePath(Object ticketId) {
        return adminPath(TICKET, "updateTicket") + "?ticketId=" + ticketId;
    }

    // frontPath(MEMBER, "update") → /holidayDessert/member/update
    public static String frontPath(String module, String page) {
        return CONTEXT_PATH + "/" + module + "/" + page;
    }

    // 組合完整上傳路徑，Paths 會自動處理各作業系統的分隔符
    // buildUploadPath(COUPON_IMAGE_FOLDER) → {admin.upload.file.path}/images/coupon/
    public static String buildUploadPath(String subFolder) {
        return Paths.get(ADMIN_UPLOAD_FILE_PATH, subFolder).toString() + File.separator;
    }

    private static String imageFolder(String table) {
        return IMAGES + File.separator + table;
    }

    private static String imageWebPath(String table) {
        return WEB_BASE + IMAGES + "/" + table + "/";
    }

}
