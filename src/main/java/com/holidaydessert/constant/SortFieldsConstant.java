package com.holidaydessert.constant;

import java.util.Map;

public class SortFieldsConstant {
	
	// Search File Default
    public static final String DEFAULT = "NEWS_CREATE";
    public static final String PD_PIC_DEFAULT = "PD_PIC_ID";
    public static final String TICKET_DEFAULT = "TICKET_ID";
    public static final String TICKET_ORDER_DEFAULT = "TICKET_ORD_ID";
    // 後台 DataTables 預設排序（主鍵，與舊寫法未排序時的資料順序一致）
    public static final String COMPANY_DEFAULT          = "COM_ID";
    public static final String DEPARTMENT_DEFAULT       = "DEPT_ID";
    public static final String COUPON_DEFAULT           = "CP_ID";
    public static final String MEMBER_COUPON_DEFAULT    = "MEM_CP_ID";
    public static final String MEMBER_DEFAULT           = "MEM_ID";
    public static final String EMPLOYEE_DEFAULT         = "EMP_ID";
    public static final String EMP_FUNCTION_DEFAULT     = "FUNC_ID";
    public static final String ORDER_DEFAULT            = "ORD_ID";
    public static final String RECEIPT_DEFAULT          = "RCP_ID";
    public static final String PROMOTION_DEFAULT        = "PM_ID";
    public static final String PROMOTION_DETAIL_DEFAULT = "PMD_ID";
    public static final String PRODUCT_DEFAULT          = "PD_ID";
	
	// 前後端排序對應關係
	public static final Map<String, String> SEARCH_NEWS = Map.of(
			// frontend field name, backend field name
            "newsId", "NEWS_ID",
            "newsName", "NEWS_NAME",
            "newsStart", "NEWS_START",
            "newsEnd", "NEWS_END",
            "newsCreate", "NEWS_CREATE"
    );
	
	public static final Map<String, String> SEARCH_NEWS_LIST = Map.ofEntries(
	        // frontend field name, backend entity field name
	        Map.entry("newsId", "NEWS_ID"),
	        Map.entry("newsName", "NEWS_NAME"),
	        Map.entry("newsStart", "NEWS_START"),
	        Map.entry("newsEnd", "NEWS_END"),
	        Map.entry("newsCreate", "NEWS_CREATE")
	);
	
	// 後台 DataTables 排序對應關係
	public static final Map<String, String> NEWS_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("NEWS_ID", "NEWS_ID"),
			Map.entry("NEWS_NAME", "NEWS_NAME"),
			Map.entry("NEWS_CONTENT", "NEWS_CONTENT"),
			Map.entry("PM_NAME", "PM_NAME"),
			Map.entry("STATUS", "NEWS_STATUS"),
			Map.entry("NEWS_START", "NEWS_START"),
			Map.entry("NEWS_END", "NEWS_END"),
			Map.entry("NEWS_CREATE", "NEWS_CREATE")
	);
	
	public static final Map<String, String> TICKET_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("TICKET_ID", "TICKET_ID"),
			Map.entry("TICKET_NAME", "TICKET_NAME"),
			Map.entry("TICKET_VENUE", "TICKET_VENUE"),
			Map.entry("TICKET_START", "TICKET_START"),
			Map.entry("TICKET_SALE_START", "TICKET_SALE_START"),
			Map.entry("TICKET_QUANTITY", "TICKET_QUANTITY"),
			Map.entry("TICKET_REMAINING", "TICKET_REMAINING"),
			Map.entry("STATUS", "TICKET_STATUS")
	);
	
	public static final Map<String, String> TICKET_ORDER_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("TICKET_ORD_ID", "TICKET_ORD_ID"),
			Map.entry("TICKET_NAME", "TICKET_NAME"),
			Map.entry("TYPE_NAME", "TYPE_NAME"),
			Map.entry("MEM_NAME", "MEM_NAME"),
			Map.entry("TICKET_ORD_QUANTITY", "TICKET_ORD_QUANTITY"),
			Map.entry("TICKET_ORD_AMOUNT", "TICKET_ORD_AMOUNT"),
			Map.entry("STATUS", "TICKET_ORD_STATUS"),
			Map.entry("TICKET_ORD_TIME", "TICKET_ORD_TIME")
	);
	
	// 公司資訊
	public static final Map<String, String> COMPANY_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("COM_ID", "COM_ID"),
			Map.entry("COM_NAME", "COM_NAME"),
			Map.entry("COM_ADDRESS", "COM_ADDRESS"),
			Map.entry("COM_PHONE", "COM_PHONE"),
			Map.entry("COM_MEMO", "COM_MEMO")
	);
	
	// 部門
	public static final Map<String, String> DEPARTMENT_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("DEPT_ID", "DEPT_ID"),
			Map.entry("DEPT_NAME", "DEPT_NAME"),
			Map.entry("DEPT_LOC", "DEPT_LOC")
	);
	
	// 優惠券
	public static final Map<String, String> COUPON_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("CP_ID", "CP_ID"),
			Map.entry("CP_NAME", "CP_NAME"),
			Map.entry("CP_DISCOUNT", "CP_DISCOUNT"),
			Map.entry("STATUS", "CP_STATUS")
	);
	
	// 會員優惠券
	public static final Map<String, String> MEMBER_COUPON_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("MEM_NAME", "MEM_NAME"),
			Map.entry("CP_NAME", "CP_NAME"),
			Map.entry("CP_DISCOUNT", "CP_DISCOUNT"),
			Map.entry("MEM_CP_START", "MEM_CP_START"),
			Map.entry("MEM_CP_END", "MEM_CP_END"),
			Map.entry("MEM_CP_STATUS", "MEM_CP_STATUS"),
			Map.entry("MEM_CP_RECORD", "MEM_CP_RECORD")
	);
	
	// 發放優惠券
	public static final Map<String, String> ISSUE_COUPON_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("MEM_NAME", "MEM_NAME"),
			Map.entry("GENDER", "GENDER"),
			Map.entry("MEM_ACCOUNT", "MEM_ACCOUNT"),
			Map.entry("MEM_PHONE", "MEM_PHONE"),
			Map.entry("MEM_EMAIL", "MEM_EMAIL"),
			Map.entry("MEM_BIRTHDAY", "MEM_BIRTHDAY"),
			Map.entry("TOTAL_EXPENSE", "TOTAL_EXPENSE")
	);
	
	// 會員
	public static final Map<String, String> MEMBER_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("MEM_ID", "MEM_ID"),
			Map.entry("MEM_NAME", "MEM_NAME"),
			Map.entry("MEM_ACCOUNT", "MEM_ACCOUNT"),
			Map.entry("MEM_PHONE", "MEM_PHONE"),
			Map.entry("MEM_EMAIL", "MEM_EMAIL"),
			Map.entry("MEM_ADDRESS", "MEM_ADDRESS"),
			Map.entry("MEM_BIRTHDAY", "MEM_BIRTHDAY"),
			Map.entry("GENDER", "GENDER")
	);
	
	// 員工（權限設定共用）
	public static final Map<String, String> EMPLOYEE_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("EMP_ID", "EMP_ID"),
			Map.entry("EMP_NAME", "EMP_NAME"),
			Map.entry("EMP_ACCOUNT", "EMP_ACCOUNT"),
			Map.entry("EMP_PHONE", "EMP_PHONE"),
			Map.entry("EMP_EMAIL", "EMP_EMAIL"),
			Map.entry("DEPT_NAME", "DEPT_NAME"),
			Map.entry("DEPT_LOC", "DEPT_LOC"),
			Map.entry("EMP_LEVEL", "EMP_LEVEL"),
			Map.entry("EMP_STATUS", "EMP_STATUS"),
			Map.entry("EMP_HIREDATE", "EMP_HIREDATE")
	);
	
	// 功能選單
	public static final Map<String, String> EMP_FUNCTION_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("FUNC_ID", "FUNC_ID"),
			Map.entry("FUNC_NAME", "FUNC_NAME"),
			Map.entry("FUNC_LINK", "FUNC_LINK"),
			Map.entry("FUNC_STATUS", "FUNC_STATUS")
	);
	
	// 訂單
	public static final Map<String, String> ORDER_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("ORD_ID", "ORD_ID"),
			Map.entry("ORD_RECIPIENT", "ORD_RECIPIENT"),
			Map.entry("MEM_GENDER", "MEM_GENDER"),
			Map.entry("MEM_EMAIL", "MEM_EMAIL"),
			Map.entry("ORD_RECIPIENT_PHONE", "ORD_RECIPIENT_PHONE"),
			Map.entry("ORD_ADDRESS", "ORD_ADDRESS"),
			Map.entry("ORD_DELIVERY", "ORD_DELIVERY"),
			Map.entry("ORD_SUBTOTAL", "ORD_SUBTOTAL"),
			Map.entry("ORD_TOTAL", "ORD_TOTAL"),
			Map.entry("ORD_DELIVERY_FEE", "ORD_DELIVERY_FEE"),
			Map.entry("ORD_STATUS", "ORD_STATUS"),
			Map.entry("ORD_PAYMENT", "ORD_PAYMENT"),
			Map.entry("ORD_NOTE", "ORD_NOTE"),
			Map.entry("COUPON_USED", "COUPON_USED"),
			Map.entry("ORD_CREATE", "ORD_CREATE")
	);
	
	// 訂單明細
	public static final Map<String, String> ORDER_DETAIL_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("ORD_ID", "ORD_ID"),
			Map.entry("ORD_RECIPIENT", "ORD_RECIPIENT"),
			Map.entry("ORD_RECIPIENT_PHONE", "ORD_RECIPIENT_PHONE"),
			Map.entry("PD_NAME", "PD_NAME"),
			Map.entry("ORDD_NUMBER", "ORDD_NUMBER"),
			Map.entry("ORDD_PRICE", "ORDD_PRICE"),
			Map.entry("ORDD_DISCOUNT_PRICE", "ORDD_DISCOUNT_PRICE"),
			Map.entry("ORD_CREATE", "ORD_CREATE")
	);
	
	// 收件資訊
	public static final Map<String, String> RECEIPT_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("RCP_ID", "RCP_ID"),
			Map.entry("MEM_ID", "MEM_ID"),
			Map.entry("RCP_NAME", "RCP_NAME"),
			Map.entry("RCP_CVS", "RCP_CVS"),
			Map.entry("RCP_ADDRESS", "RCP_ADDRESS"),
			Map.entry("RCP_PHONE", "RCP_PHONE")
	);
	
	// 購物車
	public static final Map<String, String> CART_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("MEM_NAME", "MEM_NAME"),
			Map.entry("MEM_ACCOUNT", "MEM_ACCOUNT"),
			Map.entry("GENDER", "GENDER"),
			Map.entry("MEM_PHONE", "MEM_PHONE"),
			Map.entry("MEM_EMAIL", "MEM_EMAIL"),
			Map.entry("PD_NAME", "PD_NAME"),
			Map.entry("CART_PD_QUANTITY", "CART_PD_QUANTITY")
	);
	
	// 促銷活動
	public static final Map<String, String> PROMOTION_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("PM_ID", "PM_ID"),
			Map.entry("PM_NAME", "PM_NAME"),
			Map.entry("PM_DESCRIPTION", "PM_DESCRIPTION"),
			Map.entry("PM_DISCOUNT", "PM_DISCOUNT"),
			Map.entry("PM_START", "PM_START"),
			Map.entry("PM_END", "PM_END"),
			Map.entry("PM_REGULARLY", "PM_REGULARLY"),
			Map.entry("STATUS", "PM_STATUS")
	);
	
	// 促銷明細
	public static final Map<String, String> PROMOTION_DETAIL_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("PM_NAME", "PM_NAME"),
			Map.entry("PM_STATUS", "PM_STATUS"),
			Map.entry("PD_NAME", "PD_NAME"),
			Map.entry("PD_STATUS", "PD_STATUS"),
			Map.entry("PM_DESCRIPTION", "PM_DESCRIPTION"),
			Map.entry("PM_DISCOUNT", "PM_DISCOUNT"),
			Map.entry("PD_PRICE", "PD_PRICE"),
			Map.entry("PMD_PD_DISCOUNT_PRICE", "PMD_PD_DISCOUNT_PRICE"),
			Map.entry("PMD_START", "PMD_START"),
			Map.entry("PMD_END", "PMD_END"),
			Map.entry("PM_REGULARLY", "PM_REGULARLY")
	);
	
	// 發放促銷
	public static final Map<String, String> ISSUE_PROMOTION_TABLE = Map.ofEntries(
			// DataTables columns data name, DB column name
			Map.entry("PDC_NAME", "PDC_NAME"),
			Map.entry("PD_NAME", "PD_NAME"),
			Map.entry("PD_DESCRIPTION", "PD_DESCRIPTION"),
			Map.entry("PD_PRICE", "PD_PRICE")
	);
	
}
