package com.holidaydessert.constant;

import java.util.ArrayList;
import java.util.List;

public class MemberSearchCondition {
    // operator
    public static final String EQUAL = "=";
    public static final String NOT_EQUAL = "!=";

    // suffix
    public static final String COLUMN_SUFFIX_UPPER = "_upper";

    // Member condition columns
    public static final String MEM_ID = "mem_id";                       // 會員ID
    public static final String MEM_NAME = "mem_name";                   // 姓名
    public static final String MEM_ACCOUNT = "mem_account";             // 帳號
    public static final String MEM_PASSWORD = "mem_password";           // 密碼
    public static final String MEM_GENDER = "mem_gender";               // 性別(f:女 m:男)
    public static final String MEM_PHONE = "mem_phone";                 // 電話
    public static final String MEM_EMAIL = "mem_email";                 // 信箱
    public static final String MEM_ADDRESS = "mem_address";             // 地址
    public static final String MEM_BIRTHDAY = "mem_birthday";           // 生日
    public static final String MEM_PICTURE = "mem_picture";             // 會員圖片路徑
    public static final String MEM_IMAGE = "mem_image";                 // 會員圖片名稱
    public static final String MEM_STATUS = "mem_status";               // 會員狀態(1:正常 0:停權)
    public static final String MEM_VERIFICATION_STATUS = "mem_verification_status"; // 會員驗證狀態(1:開通 0:停權)
    public static final String MEM_VERIFICATION_CODE = "mem_verification_code";     // 會員驗證碼
    public static final String MEM_GOOGLE_UID = "mem_google_uid";       // 會員googleUid
    public static final String CREATE_BY = "create_by";                 // 創建者
    public static final String CREATE_TIME = "create_time";             // 創建時間
    public static final String UPDATE_BY = "update_by";                 // 更新者
    public static final String UPDATE_TIME = "update_time";             // 更新時間

    // 日期型欄位
    public static final List<String> DATE_COLUMN_LIST = new ArrayList<String>() {{
        add(MEM_BIRTHDAY);
        add(CREATE_TIME);
        add(UPDATE_TIME);
    }};

    // 完整性欄位
    public static final List<String> INTEGRITY_COLUMN_LIST = new ArrayList<String>() {{
        add(MEM_ID);
        add(MEM_NAME);
        add(MEM_ACCOUNT);
        add(MEM_EMAIL);
        add(MEM_STATUS);
        add(CREATE_BY);
        add(CREATE_TIME);
        add(UPDATE_BY);
        add(UPDATE_TIME);
    }};

    // 模糊搜尋欄位
    public static final List<String> FUZZY_COLUMN_LIST = new ArrayList<String>() {{
        add(MEM_NAME);
        add(MEM_ACCOUNT);
        add(MEM_PHONE);
        add(MEM_EMAIL);
        add(MEM_ADDRESS);
    }};

    // 多值欄位 (可能用於IN查詢)
    public static final List<String> MULTIPLE_COLUMN_LIST = new ArrayList<String>() {{
        add(MEM_STATUS);
        add(MEM_VERIFICATION_STATUS);
        add(MEM_GENDER);
    }};

    // 數值型欄位
    public static final List<String> NUM_COLUMN_LIST = new ArrayList<String>() {{
        // 根據Member類別，沒有明確的數值型欄位
        // 如果MEM_ID是數值型，可以加入：add(MEM_ID);
    }};

    // 布林型欄位
    public static final List<String> BOOL_COLUMN_LIST = new ArrayList<String>() {{
        // 根據Member類別，可以視為布林的欄位
        add(MEM_STATUS);
        add(MEM_VERIFICATION_STATUS);
    }};
    
    // 精確匹配欄位 精確過濾 全字完全一致
    public static final List<String> EXACT_MATCH_COLUMN_LIST = new ArrayList<String>() {{
        add(MEM_ID);
        add(MEM_ACCOUNT);
        add(MEM_EMAIL);
        add(MEM_GOOGLE_UID);
    }};
    // 通配符搜尋欄位 Wildcard (通配符) 是一種模糊查詢功能，允許使用特殊字元「*」（匹配零個或多個字元）和「?」（匹配單個字元）來搜尋包含特定模式的字串。類似SQL中LIKE操作
    public static final List<String> WILDCARD_COLUMN_LIST = new ArrayList<String>() {{
        add(MEM_NAME);
        add(MEM_PHONE);
        add(MEM_ADDRESS);
    }};

}
