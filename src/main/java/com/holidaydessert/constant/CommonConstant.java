package com.holidaydessert.constant;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonConstant {

    // =============================================
    // Step 1: 用實例欄位接受 Spring 注入
    // =============================================
	@Value("${html.title}")
	public String htmlTitle;
	@Value("${mail.fromname}")
	public String fromname;
	
    // =============================================
    // Step 2: 定義 static 欄位供外部使用
    // =============================================
    public static String HTML_TITLE;
    public static String FROMNAME;

    // =============================================
    // Step 3: @PostConstruct 初始化完成後搬移值
    // =============================================
    @PostConstruct
    public void init() {
    	HTML_TITLE = this.htmlTitle;
    	FROMNAME = this.fromname;
    }
	
	public static final String ENCRYPT_KEY = "_HolidayDessert_";
    
	public final static String HOST = "smtp.gmail.com";
	public final static String AUTH = "true";
	public final static String PORT = "587";
	public final static String STARTTLE_ENABLE = "true";
//	public final static String SENDER_NAME = "假日甜點";
	public final static String SENDER = "s9017611@gmail.com";
	public final static String PASSWORD = "rkun bgju hpcf egfm";
	
}
