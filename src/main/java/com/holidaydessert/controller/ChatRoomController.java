package com.holidaydessert.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.holidaydessert.model.Message;

@Controller
public class ChatRoomController {

	private Gson gson = new Gson();

	@RequestMapping(value = "/front/chat", method = { RequestMethod.GET, RequestMethod.POST })
	public String chatPage() {
		return "front/chat";
	}
	
	@MessageMapping("/chat")
	@SendTo("/topic/greetings")
	public String send(Message message) throws Exception {

		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		System.out.println(message.getMemId());
		System.out.println(message.getMsgDirection());
		message.setMsgTime(time);
		Thread.sleep(200);
		
		String output = gson.toJson(message);
		
		return output;
	}
	
}
