package com.holidaydessert.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.holidaydessert.model.Member;
import com.holidaydessert.model.Message;

@Controller
public class MessageController {
	
	@RequestMapping(value = "/front/chat", method = { RequestMethod.GET, RequestMethod.POST })
    public String chatPage() {
        return "front/chat";
    }
	
	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public Message send(Message message) throws Exception {
	    String time = new SimpleDateFormat("HH:mm").format(new Date());
	    System.out.println(message);
	    return new Message(message.getEmpId(), message.getMsgContent(), time);
	}
	
	@MessageMapping("/message")
	@SendTo("topic/getResponse")
	public Message said(Member responseMessage) throws InterruptedException {
		Thread.sleep(3000);
		return new Message("歡迎來到," + responseMessage.getMemName());
	}

}
