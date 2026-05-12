package com.holidaydessert.constant;

import java.io.File;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BuildPath {

    // =============================================
    // Step 1: 用實例欄位接受 Spring 注入
    // =============================================
    @Value("${admin.upload.file.path}")
    private String adminUploadFilePath;
	@Value("${web.path}")
	public String webPath;
	
    // =============================================
    // Step 2: 定義 static 欄位供外部使用
    // =============================================
    public static String ADMIN_UPLOAD_FILE_PATH;
    public static String WEB_PATH;

    // =============================================
    // Step 3: @PostConstruct 初始化完成後搬移值
    // =============================================
    @PostConstruct
    public void init() {
        ADMIN_UPLOAD_FILE_PATH = this.adminUploadFilePath;
        WEB_PATH = this.webPath;
    }
    
    // =============================================
    // Fold
    // =============================================
    public static final String IMAGES     = "images";
    // Table
    public static final String COUPON     = "coupon";
    public static final String EMPLOYEE   = "employee";
    public static final String BANNER     = "banner";
    public static final String PRODUCTPIC = "productPic";
	
    // =============================================
    // Upload Sub Paths
    // =============================================
    public static final String COUPON_IMAGE_FOLDER     = IMAGES + File.separator + COUPON;
    public static final String EMPLOYEE_IMAGE_FOLDER   = IMAGES + File.separator + EMPLOYEE;
    public static final String BANNER_IMAGE_FOLDER     = IMAGES + File.separator + BANNER;
    public static final String PRODUCTPIC_IMAGE_FOLDER = IMAGES + File.separator + PRODUCTPIC;

    // =============================================
    // Web Access Paths
    // =============================================
    private static final String WEB_BASE = "holidayDessert/admin/upload/";

    public static final String COUPON_WEB_PATH     = WEB_BASE + IMAGES + "/" + COUPON     + "/";
    public static final String EMPLOYEE_WEB_PATH   = WEB_BASE + IMAGES + "/" + EMPLOYEE   + "/";
    public static final String BANNER_WEB_PATH     = WEB_BASE + IMAGES + "/" + BANNER     + "/";
    public static final String PRODUCTPIC_WEB_PATH = WEB_BASE + IMAGES + "/" + PRODUCTPIC + "/";

    // =============================================
    // 工具方法：組合完整上傳路徑
    // =============================================
    /**
     * 組合完整上傳路徑（自動處理 OS 分隔符，不需要手動判斷 Windows/Linux/Mac）
     *
     * @param adminUploadFilePath application.properties 的 admin.upload.file.path
     * @param subFolder           PathConstants 中定義的子路徑常數
     * @return 完整的上傳路徑字串
     */
    public static String buildUploadPath(String subFolder) {
        return Paths.get(ADMIN_UPLOAD_FILE_PATH, subFolder).toString() + File.separator;
    }
    
}
