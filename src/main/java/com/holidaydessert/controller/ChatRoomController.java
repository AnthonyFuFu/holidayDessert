package com.holidaydessert.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

import com.holidaydessert.model.ChatRoom;
import com.holidaydessert.model.Message;

@Controller
public class ChatRoomController {

	@RequestMapping(value = "/front/chat", method = { RequestMethod.GET, RequestMethod.POST })
	public String chatPage() {
		return "front/chat";
	}

	@MessageMapping("/chat")
	@SendTo("/topic/greetings")
	public ChatRoom send(Message message) throws Exception {
		String time = new SimpleDateFormat("HH:mm").format(new Date());
		System.out.println(time);
		Thread.sleep(200);
		return new ChatRoom(HtmlUtils.htmlEscape(message.getMsgContent()));
	}

}
