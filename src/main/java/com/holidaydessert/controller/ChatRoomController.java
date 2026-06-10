package com.holidaydessert.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.Message;
import com.holidaydessert.service.ChatRoomService;
import com.holidaydessert.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
public class ChatRoomController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ChatRoomService chatRoomService;
	
    @PostMapping(value = "/claimChatRoom")
	@Operation(summary = "認領聊天室", description = "誰先點誰負責，將 NULL EMP_ID 更新為當前員工")
	public ResponseEntity<?> claimChatRoom(
			@Parameter(name = "Message", description = "包含 roomId 與 empId", required = true) @RequestBody Message message) {

		ApiReturnObject apiReturnObject = chatRoomService.claimChatRoom(message.getRoomId(), message.getEmpId());
		return new ResponseEntity<>(apiReturnObject, HttpStatus.OK);
	}

    @PostMapping(value = "/getChatRoom")
	@Operation(summary = "獲取聊天室", description = "獲取聊天室，有則聊天，沒有則建立")
	public ResponseEntity<?> getChatRoom(
			@Parameter(name = "Member", description = "會員", required = true) @RequestBody Member member) {
    	
    	Integer memId = member.getMemId();
		ApiReturnObject apiReturnObject = chatRoomService.getChatRoom(memId);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}
    
    @PostMapping(value = "/getMessageByEmp")
	@Operation(summary = "取得客服對會員對話紀錄", description = "取得客服對會員對話紀錄，發送方向(0:客服對會員 1:會員對客服)")
	public ResponseEntity<?> getMessageByEmp(
			@Parameter(name = "Employee", description = "客服", required = true) @RequestBody Message message) {
    	
		ApiReturnObject apiReturnObject = messageService.getMessageByEmp(message);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}

    @PostMapping(value = "/getMessageByMem")
	@Operation(summary = "取得會員對客服對話紀錄", description = "取得會員對客服對話紀錄，發送方向(0:客服對會員 1:會員對客服)")
	public ResponseEntity<?> getMessageByMem(
			@Parameter(name = "Member", description = "會員", required = true) @RequestBody Message message) {
    	
		ApiReturnObject apiReturnObject = messageService.getMessageByMem(message);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
	}
    
    @MessageMapping("/chat/{roomUrl}")  // 前端 send 的路徑
    @SendTo("/topic/chat/{roomUrl}")    // 前端 subscribe 的路徑
    public Message send(@DestinationVariable String roomUrl, Message message) {
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		message.setMsgTime(time);
        messageService.saveMessage(message);
        return message;
    }
	
}
