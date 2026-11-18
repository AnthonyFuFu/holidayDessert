package com.holidaydessert.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.holidaydessert.model.Employee;
import com.holidaydessert.service.CartService;
import com.holidaydessert.service.ChatRoomService;
import com.holidaydessert.service.MemberService;
import com.holidaydessert.service.ReceiptInformationService;
import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin/member")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class MemberManagement {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ReceiptInformationService receiptInformationService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ChatRoomService chatRoomService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		List<Map<String, Object>> chatRoomList = chatRoomService.getAllChatRoom();
		model.addAttribute("chatRoomList", chatRoomList);
		
		return "admin/member/list";

	}
	
	@ResponseBody
	@GetMapping("/memberTables")
	public Map<String, Object> memberTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return memberService.list(params);
	}
	
	@ResponseBody
	@GetMapping("/receiptInformationTables")
	public Map<String, Object> receiptInformationTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return receiptInformationService.list(params);
	}
	
	@ResponseBody
	@GetMapping("/cartTables")
	public Map<String, Object> cartTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return cartService.list(params);
	}
	
}
