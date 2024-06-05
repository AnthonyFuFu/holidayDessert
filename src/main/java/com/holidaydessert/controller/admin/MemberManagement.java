package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Cart;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Member;
import com.holidaydessert.model.ReceiptInformation;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CartService;
import com.holidaydessert.service.ChatRoomService;
import com.holidaydessert.service.MemberService;
import com.holidaydessert.service.ReceiptInformationService;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/admin/member")
@SessionAttributes("employeeSession")
@CrossOrigin
@ApiIgnore
public class MemberManagement {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ReceiptInformationService receiptInformationService;

	@Autowired
	private CartService cartService;

	@Autowired
	private ChatRoomService chatRoomService;
	
	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		List<Map<String, Object>> chatRoomList = chatRoomService.getAllChatRoom();
		
		model.addAttribute("chatRoomList", chatRoomList);
		model.addAttribute("authorityList", authorityList);
		return "admin/member/list";

	}
	
	@GetMapping("/memberTables")
	public void memberTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Member member, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Member memberData = new Member();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		memberData.setStart(start);
		memberData.setLength(length);
		memberData.setSearchText(searchValue);
		
		List<Map<String, Object>> memberList = memberService.list(memberData);

		if (memberList == null) {
			memberList = new ArrayList<Map<String, Object>>();
		}

		int count = memberService.getCount(memberData);

		member.setRecordsFiltered(count);
		member.setRecordsTotal(count);
		member.setData(memberList);
		member.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(member);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@GetMapping("/receiptInformationTables")
	public void receiptInformationTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ReceiptInformation receiptInformation, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		ReceiptInformation receiptInformationData = new ReceiptInformation();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		receiptInformationData.setStart(start);
		receiptInformationData.setLength(length);
		receiptInformationData.setSearchText(searchValue);
		
		List<Map<String, Object>> receiptInformationList = receiptInformationService.list(receiptInformationData);

		if (receiptInformationList == null) {
			receiptInformationList = new ArrayList<Map<String, Object>>();
		}

		int count = receiptInformationService.getCount(receiptInformationData);

		receiptInformation.setRecordsFiltered(count);
		receiptInformation.setRecordsTotal(count);
		receiptInformation.setData(receiptInformationList);
		receiptInformation.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(receiptInformation);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@GetMapping("/cartTables")
	public void cartTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Cart cart, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Cart cartData = new Cart();
		
		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		cartData.setStart(start);
		cartData.setLength(length);
		cartData.setSearchText(searchValue);
		
		List<Map<String, Object>> cartList = cartService.list(cartData);

		if (cartList == null) {
			cartList = new ArrayList<Map<String, Object>>();
		}
		
		int count = cartService.getCount(cartData);

		cart.setRecordsFiltered(count);
		cart.setRecordsTotal(count);
		cart.setData(cartList);
		cart.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(cart);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
