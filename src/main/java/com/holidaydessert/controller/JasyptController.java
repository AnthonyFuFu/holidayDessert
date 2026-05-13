package com.holidaydessert.controller;

import com.holidaydessert.model.ApiReturnObject;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.NoIvGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/jasypt")
public class JasyptController {

    @Value("${jasypt.encryptor.algorithm:PBEWithMD5AndDES}")
    private String algorithm;

    @Value("${jasypt.encryptor.password:123456}")
    private String password;

    // =============================================
    // 加密
    // POST /jasypt/encrypt
    // http://localhost:8080/holidayDessert/jasypt/encrypt?text=123456
    // =============================================
    @PostMapping("/encrypt")
    public ApiReturnObject encrypt(@RequestParam String text) {
        try {
            String result = getEncryptor().encrypt(text);

            Map<String, String> data = new LinkedHashMap<>();
            data.put("algorithm", algorithm);
            data.put("input",     text);
            data.put("output",    result);
            data.put("ENC格式",   "ENC(" + result + ")");

            log.info("Jasypt 加密成功 input:{}", text);
            return ApiReturnObject.success("加密成功", data);

        } catch (Exception e) {
            log.error("Jasypt 加密失敗", e);
            return ApiReturnObject.serverError("加密失敗：" + e.getMessage());
        }
    }

    // =============================================
    // 解密
    // POST /jasypt/decrypt
    // http://localhost:8080/holidayDessert/jasypt/decrypt?text=ENC(8gMl7xHuEg8s/7RKMaikaA==)
    // =============================================
    @PostMapping("/decrypt")
    public ApiReturnObject decrypt(@RequestParam String text) {
        try {
            // 自動去除 ENC() 包裝
            String cleanText = text.trim()
                                   .replace("ENC(", "")
                                   .replace(")", "");

            String result = getEncryptor().decrypt(cleanText);

            Map<String, String> data = new LinkedHashMap<>();
            data.put("algorithm", algorithm);
            data.put("input",     text);
            data.put("output",    result);

            log.info("Jasypt 解密成功");
            return ApiReturnObject.success("解密成功", data);

        } catch (Exception e) {
            log.error("Jasypt 解密失敗", e);
            return ApiReturnObject.serverError("解密失敗：" + e.getMessage());
        }
    }

    // =============================================
    // 工具方法：建立 Encryptor
    // =============================================
    private StandardPBEStringEncryptor getEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setIvGenerator(new NoIvGenerator());
        encryptor.setAlgorithm(algorithm);
        encryptor.setPassword(password);
        return encryptor;
    }
//  ENC(8gMl7xHuEg8s/7RKMaikaA==) // 123456
    
//  -Djasypt.encryptor.password=123456
//  -Djasypt.encryptor.algorithm=PBEWithMD5AndDES
//  -Djasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator
//  -Djavax.net.ssl.trustStore="C:/Program Files/Java/jdk-11/lib/security/cacerts"
}