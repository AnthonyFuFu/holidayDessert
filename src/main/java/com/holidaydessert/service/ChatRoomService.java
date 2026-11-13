package com.holidaydessert.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holidaydessert.dao.ChatRoomDao;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.ChatRoom;
import com.holidaydessert.repository.ChatRoomRepository;

@Service
public class ChatRoomService {

	@Autowired
	private ChatRoomDao chatRoomDao;
	
    @Autowired
    private ChatRoomRepository chatRoomRepository;

	// =============================================
	// back
	// =============================================
	public List<Map<String, Object>> getAllChatRoom() {
		return chatRoomDao.getAllChatRoom();
	}

	@Transactional
	public ApiReturnObject claimChatRoom(String roomId, String empId) {
		int claimed = chatRoomRepository.claimChatRoom(empId, roomId);
		String msg = claimed > 0 ? "已認領聊天室" : "聊天室已有負責人";
		return ApiReturnObject.success(msg, claimed);
	}

	// =============================================
	// front
	// =============================================
	// get-or-create：有則取得，沒有則建立
	// 刻意不加 @Transactional：建立時若撞到 UNIQUE(MEM_ID)，需要在新的交易裡重新查詢
	public ApiReturnObject getChatRoom(Integer memId) {
		if (memId == null) {
			return ApiReturnObject.badRequest("缺少會員ID");
		}

		ChatRoom existing = chatRoomRepository.findByMemId(memId).orElse(null);
		if (existing != null) {
			return ApiReturnObject.success("取得聊天室", chatRoomRepository.getChatRoom(existing.getRoomId()));
		}

		Long roomId = createChatRoom(memId);
		return ApiReturnObject.success("建立新聊天室", chatRoomRepository.getChatRoom(roomId));
	}

	private Long createChatRoom(Integer memId) {
		// 用 UUID：只含 [0-9a-f-]，放進 STOMP 路徑 /app/chat/{roomUrl} 不會被 '/' 切斷
		String roomUrl = UUID.randomUUID().toString();
		try {
			return chatRoomRepository.saveAndFlush(ChatRoom.createNew(memId, roomUrl)).getRoomId();
		} catch (DataIntegrityViolationException e) {
			// 同一會員同時發出兩個建立請求（連點、多分頁），另一個請求已先建好：改為取得那一間
			return chatRoomRepository.findByMemId(memId)
					.map(ChatRoom::getRoomId)
					.orElseThrow(() -> e);
		}
	}
	
	public ApiReturnObject getServiceStaff(Integer memId) {
		List<Map<String, Object>> newArrivalList = chatRoomRepository.getServiceStaff(memId);
		
		if(newArrivalList == null) {
			return ApiReturnObject.success("建立新聊天室", null);
		}
		
		return ApiReturnObject.success("取得聊天室", newArrivalList);
	}
	
}
