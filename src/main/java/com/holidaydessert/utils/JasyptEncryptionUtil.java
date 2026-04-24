package com.holidaydessert.utils;

import java.util.HashMap;
import java.util.Scanner;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.NoIvGenerator;

public class JasyptEncryptionUtil {
    public static void main(String[] args) {
        String encryptText = "";
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("1", "encrypt");
        options.put("2", "decrypt");

        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setIvGenerator(new NoIvGenerator());
        standardPBEStringEncryptor.setAlgorithm(System.getProperty("jasypt.encryptor.algorithm", "PBEWithMD5AndDES"));
        standardPBEStringEncryptor.setPassword(System.getProperty("jasypt.encryptor.password", "123456"));

        System.out.print("Input 1 to encrypt. Input 2 to decrypt: ");
        Scanner sc = new Scanner(System.in);
        String choose_decryt_encrypt = sc.nextLine();

        System.out.print("Enter the text you want to " + options.get(choose_decryt_encrypt) + ": ");
        sc = new Scanner(System.in);
        String inputText = sc.nextLine();

        System.out.println("\n ----ARGUMENTS-------------------");
        System.out.println("algorithm: " + System.getProperty("jasypt.encryptor.algorithm", "PBEWithMD5AndDES"));
        System.out.println("input: " + inputText + "");
        System.out.println("password: " + System.getProperty("jasypt.encryptor.password", "123456"));

        if("1".equals(choose_decryt_encrypt)){
            encryptText = standardPBEStringEncryptor.encrypt(inputText);
        }else{
            encryptText = standardPBEStringEncryptor.decrypt(inputText);
        }

        System.out.println("\n----OUTPUT----------------------");
        System.out.println(encryptText);
    }
//    ENC(8gMl7xHuEg8s/7RKMaikaA==) // 123456
    
//    -Djasypt.encryptor.password=123456
//    -Djasypt.encryptor.algorithm=PBEWithMD5AndDES
//    -Djasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator
//    -Djavax.net.ssl.trustStore="C:/Program Files/Java/jdk-11/lib/security/cacerts"
}
