package com.holidaydessert.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.ChatRoomDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.ChatRoom;
import com.holidaydessert.model.Message;
import com.holidaydessert.repository.ChatRoomRepository;
import com.holidaydessert.repository.MessageRepository;
import com.holidaydessert.service.ChatRoomService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.MessageService;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

	@Autowired
	private ChatRoomDao chatRoomDao;
	
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
	@Autowired
	private MessageService messageService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private MessageRepository messageRepository;
	
	@Override
	public List<Map<String, Object>> getAllChatRoom() {
		return chatRoomDao.getAllChatRoom();
	}

	@Override
	@Transactional
	public ApiReturnObject claimChatRoom(String roomId, String empId) {
		int claimed = messageRepository.claimMessages(empId, roomId);
		String msg = claimed > 0 ? "已認領聊天室" : "聊天室已有負責人";
		return ApiReturnObject.success(msg, claimed);
	}

	@Override
	public ApiReturnObject getChatRoom(Integer memId) {
		List<Map<String, Object>> chatRoomByMessage = chatRoomRepository.getChatRoomByMessage(memId);

		if (chatRoomByMessage == null || chatRoomByMessage.isEmpty()) {
			try {
		        String roomUrl = commonService.generateEncryptedToken(memId.toString());
		        Long roomId = chatRoomRepository.save(ChatRoom.createNew(roomUrl)).getRoomId();

				String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				Message message = new Message();
				message.setEmpId(null);       // 尚未分派員工，設為 null
				message.setMemId(memId);
				message.setRoomId(roomId.toString());
				message.setMsgContent(null);  // null 代表佔位訊息，不顯示在前端
				message.setMsgTime(dateTime);
				message.setMsgDirection("1");
				messageService.saveMessage(message);

				List<Map<String, Object>> newchatRoomByMessage = chatRoomRepository.getChatRoomByMessage(memId);
				
				Long newChatRoomId = newchatRoomByMessage.stream()
				        .map(m -> ((Number) m.get("ROOM_ID")).longValue())
				        .findFirst()
				        .orElse(null);
				
				List<Map<String, Object>> newChatRoom = chatRoomRepository.getChatRoom(newChatRoomId.toString());
				return ApiReturnObject.success("建立新聊天室", newChatRoom);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Long chatRoomId = chatRoomByMessage.stream()
		        .map(m -> ((Number) m.get("ROOM_ID")).longValue())
		        .findFirst()
		        .orElse(null);

		List<Map<String, Object>> chatRoom = chatRoomRepository.getChatRoom(chatRoomId.toString());
		
		return ApiReturnObject.success("取得聊天室", chatRoom);
	}
	
	@Override
	public ApiReturnObject getServiceStaff(Integer memId) {
		List<Map<String, Object>> newArrivalList = chatRoomRepository.getServiceStaff(memId);
		
		if(newArrivalList == null) {
			return ApiReturnObject.success("建立新聊天室", null);
		}
		
		return ApiReturnObject.success("取得聊天室", newArrivalList);
	}
	
}
