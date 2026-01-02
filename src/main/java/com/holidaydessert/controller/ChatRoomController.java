package com.holidaydessert.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.holidaydessert.model.ApiReturnObject;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.Message;
import com.holidaydessert.service.ChatRoomService;
import com.holidaydessert.service.MessageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class ChatRoomController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ChatRoomService chatRoomService;
	
	private Gson gson = new Gson();
	
    @PostMapping(value = "/getChatRoom")
	@ApiOperation(value = "獲取聊天室", notes = "獲取聊天室，有則聊天，沒有則建立")
	public ResponseEntity<?> getChatRoom(
			@ApiParam(name = "Member", value = "會員", required = true) @RequestBody Member member) {
    	
    	String memId = member.getMemId();
		ApiReturnObject apiReturnObject = chatRoomService.getChatRoom(memId);
		return new ResponseEntity<ApiReturnObject>(apiReturnObject,HttpStatus.OK);
		
	}
    
    @PostMapping(value = "/getMessageByEmp")
	@ApiOperation(value = "取得客服對會員對話紀錄", notes = "取得客服對會員對話紀錄，發送方向(0:客服對會員 1:會員對客服)")
	public ResponseEntity<?> getMessageByEmp(
			@ApiParam(name = "Employee", value = "客服", required = true) @RequestBody Message message) {
    	
		ApiReturnObject apiReturnObject = messageService.getMessageByEmp(message);
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
    
    
    
    
	@RequestMapping(value = "/front/chat", method = { RequestMethod.GET, RequestMethod.POST })
	public String chatPage() {
		return "front/chat";
	}
	
	@MessageMapping("/chat")
	@SendTo("/topic/greetings")
	public String send(Message message) throws Exception {

		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		System.out.println(message.getEmpId());
		System.out.println(message.getMemId());
		System.out.println(message.getMsgDirection());
		message.setMsgTime(time);
//		messageService.saveMessage(message);
		
		System.out.println(messageService.getMessageByMemId(message));
//		Thread.sleep(200);
		
		String output = gson.toJson(message);
		
		return output;
	}
	
}
