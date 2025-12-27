package com.holidaydessert.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.holidaydessert.dao.ChatRoomDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Message;
import com.holidaydessert.service.ChatRoomService;
import com.holidaydessert.service.MessageService;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

	@Autowired
	private ChatRoomDao chatRoomDao;
	
	@Autowired
	private MessageService messageService;
	
	@Override
	public List<Map<String, Object>> getAllChatRoom() {
		return chatRoomDao.getAllChatRoom();
	}

	@Override
	public ApiReturnObject getChatRoom(String memId) {
		List<Map<String, Object>> chatRoomByMessage = chatRoomDao.getChatRoomByMessage(memId);
		
		if (chatRoomByMessage == null) {
			try {
				String cKey = "_HolidayDessert_";
				Calendar date = Calendar.getInstance();
				DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMddHHmmss");
				String yyyymmddStr = yyyymmdd.format(date.getTime());
				String temp = memId + "," + yyyymmddStr;
				String roomUrl = encrypt(temp, cKey);
				Long roomId = chatRoomDao.addChatRoom(roomUrl);
				
				String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				Message message = new Message();
				message.setEmpId("0");
				message.setMemId(memId);
				message.setRoomId(roomId.toString());
				message.setMsgContent("");
				message.setMsgTime(dateTime);
				message.setMsgDirection("1");
				messageService.saveMessage(message);

				List<Map<String, Object>> newchatRoomByMessage = chatRoomDao.getChatRoomByMessage(memId);
				
				Long newChatRoomId = newchatRoomByMessage.stream()
				        .map(m -> ((Number) m.get("ROOM_ID")).longValue())
				        .findFirst()
				        .orElse(null);
				
				List<Map<String, Object>> newChatRoom = chatRoomDao.getChatRoom(newChatRoomId.toString());
				return new ApiReturnObject(200, "建立新聊天室", newChatRoom);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Long chatRoomId = chatRoomByMessage.stream()
		        .map(m -> ((Number) m.get("ROOM_ID")).longValue())
		        .findFirst()
		        .orElse(null);

		List<Map<String, Object>> chatRoom = chatRoomDao.getChatRoom(chatRoomId.toString());
		
		return new ApiReturnObject(200, "取得聊天室", chatRoom);
	}
	
	@Override
	public ApiReturnObject getServiceStaff(String memId) {
		List<Map<String, Object>> newArrivalList = chatRoomDao.getServiceStaff(memId);
		
		if(newArrivalList == null) {
			return new ApiReturnObject(200, "建立新聊天室", null);
		}
		
		return new ApiReturnObject(200, "取得聊天室", newArrivalList);
	}
	
	public static String encrypt(String sSrc, String sKey) throws Exception {
		Base64.Encoder encoder = Base64.getEncoder();
		if (sKey == null) {
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());

		return encoder.encodeToString(encrypted);
	}
}
