package com.holidaydessert.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.ui.Model;

/**
 * 常用類別 使用Static靜態 就不需要new util使用來放常用類別 ,放在此package底下基本上不需要給予@Bean的宣告就能使用
 * 因為都是靜態的屬性與方法
 * 
 * @author tkbuser
 *
 */
public class BaseUtil {
	protected static String passwordKey = "LEARNKEY";

	// 回傳加密字串
	public static String encryptStr(String inputWord) {
		String encryptResult = "";
		try {
			encryptResult = encrypt(inputWord, passwordKey);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return encryptResult;

	}

	// 回傳解密字串
	public static String decryptStr(String inputWord) {
		String decryptResult = "";
		try {
			decryptResult = decrypt(inputWord, passwordKey);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return decryptResult;

	}

	/**
	 * 加密方法
	 */
	public static String encrypt(String content, String key) {
		try {

			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] byteRresult = cipher.doFinal(byteContent);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteRresult.length; i++) {
				String hex = Integer.toHexString(byteRresult[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密方法
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String content, String key) throws UnsupportedEncodingException {
		if (content.length() < 1)
			return null;
		byte[] byteRresult = new byte[content.length() / 2];
		for (int i = 0; i < content.length() / 2; i++) {
			int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
			byteRresult[i] = (byte) (high * 16 + low);
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] result = cipher.doFinal(byteRresult);
			return new String(result, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 科目設定樣式
	public static String setSubjectStyle(String str) {

		String result = str;
		if (result.indexOf("國") >= 0) {
			result = result.replaceAll("國", "<span class=\"sub-wrap color-ch\">國</span>");
		}

		if (result.indexOf("英") >= 0) {
			result = result.replaceAll("英", "<span class=\"sub-wrap color-en\">英</span>");
		}

		if (result.indexOf("數") >= 0) {
			if (result.indexOf("數A") >= 0) {
				result = result.replaceAll("數A", "<span class=\"sub-wrap color-mathA\">數A</span>");
			} else if (result.indexOf("數B") >= 0) {
				result = result.replaceAll("數B", "<span class=\"sub-wrap color-mathB\">數B</span>");
			} else {
				result = result.replaceAll("數", "<span class=\"sub-wrap color-mathA\">數</span>");
			}
		}

		if (result.indexOf("社") >= 0) {
			result = result.replaceAll("社", "<span class=\"sub-wrap color-so\">社</span>");
		}

		if (result.indexOf("自") >= 0) {
			result = result.replaceAll("自", "<span class=\"sub-wrap color-na\">自</span>");
		}

		return result;

	}

	public static void setAuthority(Map<String, Object> map, Model model) {

		String action = map.get("icon") != null ? map.get("icon").toString() : "";

		switch (action) {
		case "C":
			// 新增
			model.addAttribute("C", "T");
			break;
		case "R":
			// 查詢
			model.addAttribute("R", "T");
			break;
		case "U":
			// 修改
			model.addAttribute("U", "T");
			break;
		case "D":
			// 刪除
			model.addAttribute("D", "T");
			break;
		case "I":
			// 匯入
			model.addAttribute("I", "T");
			break;
		case "E":
			// 匯出
			model.addAttribute("E", "T");
			break;
		case "ON":
			// 上架
			model.addAttribute("ON", "T");
			break;
		case "OFF":
			// 下架
			model.addAttribute("OFF", "T");
			break;
		case "SUCCESS":
			// 驗證成功
			model.addAttribute("SUCCESS", "T");
			break;
		case "FAILURE":
			// 驗證失敗
			model.addAttribute("FAILURE", "T");
			break;
		case "NST":
			// 更新至學校表
			model.addAttribute("NST", "T");
			break;
		case "NDE":
			// 更新校系推估
			model.addAttribute("NDE", "T");
			break;
		}
	}
}