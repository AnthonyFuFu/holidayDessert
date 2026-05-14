package com.holidaydessert.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.view.RedirectView;

import com.holidaydessert.model.Member;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.MainOrderService;
import com.holidaydessert.service.MemberService;
import static com.holidaydessert.constant.BuildPath.*;
import static com.holidaydessert.constant.CommonConstant.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/member")
@Api(tags = "會員")
public class MemberController {

	private String getEmailContents(String code) {
		return "<div>歡迎您註冊假日甜點</div><br><br>"
			 + "<div>請點擊下列網址以驗證Email信箱:</div><br>" + WEB_PATH
			 + "holidayDessert/member/verificationEmail?code=" + code;
	}
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MainOrderService mainOrderService;
	
	@Autowired
	private CommonService commonService;

	@PostMapping(value = "/register")
	@ApiOperation(value = "註冊", notes = "註冊會員資料")
	public ResponseEntity<?> register(@ApiParam(name = "Member", value = "會員", required = true) @RequestBody Member member) {
		Map<String, Object> responseMap = new HashMap<>();
		try{
	        String content = commonService.generateEncryptedToken(member.getMemEmail());
			String contents = getEmailContents(URLEncoder.encode( content, "UTF-8" ));
			Member user = memberService.getCheckMemberEmail(member);
			
			if(user == null) {
				member.setMemAccount(member.getMemEmail());
				member.setMemVerificationStatus("0");
				member.setMemVerificationCode(URLEncoder.encode( content, "UTF-8" ));
				memberService.register(member);
				//測試拿掉，上正式要打開
				commonService.sendGmail(member.getMemEmail(), HTML_TITLE+"-帳戶電子郵件驗證", contents);
				responseMap.put("STATUS", "T");
				responseMap.put("MSG", "恭喜你成功註冊為假日甜點會員");
			} else {
				responseMap.put("STATUS", "F");
				responseMap.put("MSG", "請勿重複註冊");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok(responseMap);
	}

	@PostMapping(value = "/checkMemberAccountEmail")
	@ApiOperation(value = "驗證信箱是否重複使用", notes = "驗證信箱是否重複使用")
	public ResponseEntity<?> checkMemberAccountEmail(
			@ApiParam(name = "Member", value = "會員", required = true) @RequestBody Member member) {
		Map<String, Object> responseMap = new HashMap<>();
		Member checkMemberEmail = memberService.getCheckMemberEmail(member);
		if (checkMemberEmail == null) {
			responseMap.put("STATUS", "T");
			responseMap.put("MSG", "此email可以使用");
		} else {
			responseMap.put("STATUS", "F");
			responseMap.put("MSG", "此email已經註冊,請選擇其他email");
		}
		return ResponseEntity.ok(responseMap);
	}
	
	@PostMapping(value = "/reSendEmail")
	@ApiOperation(value = "重寄驗證信", notes = "重寄註冊驗證信")
	public ResponseEntity<?> reSendEmail(
			@ApiParam(name = "Member", value = "會員", required = true) @RequestBody Member member) {
		Map<String, Object> responseMap = new HashMap<>();
		try {
			member = memberService.getCheckMemberEmail(member);
	        String content = commonService.generateEncryptedToken(member.getMemEmail());
			String contents = getEmailContents(URLEncoder.encode( content, "UTF-8" ));
			
			if (member != null) {
				if ("1".equals(member.getMemVerificationStatus())) {
					responseMap.put("STATUS", "T");
					responseMap.put("MSG", "信箱已驗證過");
				} else {
					member.setMemVerificationCode(URLEncoder.encode(content, "UTF-8"));
					// 測試拿掉，上正式要打開
					commonService.sendGmail(member.getMemEmail(), HTML_TITLE + "-帳戶電子郵件驗證", contents);
					memberService.updateVerification(member);
					responseMap.put("STATUS", "RS");
					responseMap.put("MSG", "驗證信已寄出");
				}
			}
		} catch (Exception e) {
			responseMap.put("STATUS", "F");
			responseMap.put("MSG", "驗證信寄出異常");
			e.printStackTrace();
		}
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping(value = "/verificationEmail")
	@ApiOperation(value = "信箱驗證", notes = "註冊信箱驗證連結")
	public RedirectView verificationEmail(
	        @ApiParam(name = "code", value = "驗證碼", required = true)
	        @RequestParam(required = true) String code) throws NullPointerException {
    	RedirectView redirectView = new RedirectView();
		try {
			String content = commonService.decryptToken(code);
			String[] text = content.split(",");
			Member member = new Member();
			member.setMemEmail(text[0]);
			
			member = memberService.getCheckMemberEmail(member);
			memberService.verificationEmail(member);
	    	redirectView.setUrl("/holidayDessert/member/verificationSuccess.html"); // 設置要跳轉的URL
			
		} catch (UnsupportedEncodingException e1) {
	    	redirectView.setUrl("/holidayDessert/member/verification.html"); // 設置要跳轉的URL
		} catch (Exception e) {
	    	redirectView.setUrl("/holidayDessert/member/verification.html"); // 設置要跳轉的URL
		}
    	return redirectView;
	}
	
    @ApiIgnore
	@RequestMapping(value = "/update" , method = {RequestMethod.GET, RequestMethod.POST} , produces="text/plain;charset=UTF-8")
	public String update(@SessionAttribute Member memberSession, @ModelAttribute Member member, HttpSession session, Model model) {
		
		try {
			if(memberSession == null) {
				model.addAttribute("PATH", "/holidayDessert/index"); 
				return "front/toPath";
			}
			// 取得舊資料
			Member user = new Member();
			user = memberService.getCheckMemberEmail(memberSession);
			model.addAttribute("member", user);
			
			List<Map<String, Object>> orderChildList = mainOrderService.getMemOrderList(user.getMemId());
			model.addAttribute("orderChildList", orderChildList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "front/member/update";
	}

    @ApiIgnore
    @PostMapping(value = "/updateSubmit" , produces="text/plain;charset=UTF-8")
	public String updateSubmit(@SessionAttribute Member memberSession, @ModelAttribute Member member, HttpSession session, Model model, HttpServletRequest pRequest) {
		
		member.setMemEmail(memberSession.getMemEmail());
		
		try{
			memberService.edit(member);
			model.addAttribute("message", " 修改會員資料成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("PATH", "/holidayDessert/member/update");
		return "front/toPath";
	}
	
//	@RequestMapping(value = "/updatePassword" , method = {RequestMethod.GET, RequestMethod.POST})
//	public String updatePassword(@SessionAttribute("memberSession") Member memberSession, @ModelAttribute Member member, HttpSession session, Model model) {
//		return "front/member/updatePassword";
//	}

    @ApiIgnore
    @PostMapping(value = "/updatePasswordSubmit")
	public String updatePasswordSubmit(@SessionAttribute Member memberSession, @ModelAttribute Member member, HttpSession session, Model model, HttpServletRequest pRequest) {
		
		member.setMemEmail(memberSession.getMemEmail());
		
		try{
			memberService.updatePassword(member);
			model.addAttribute("MESSAGE", " 修改密碼成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("PATH", "/forecast/member/update");
		return "front/toPath";
	}

    @ApiIgnore
	@RequestMapping(value = "/checkPassword" , method = {RequestMethod.GET, RequestMethod.POST})
	public void checkPassword(@SessionAttribute Member memberSession, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {

		String status = "T";
		String password = pRequest.getParameter("oldPW") == null ? "" : pRequest.getParameter("oldPW");
		
		Member member = new Member();
		member.setMemEmail(memberSession.getMemEmail());
		member.setMemPassword(password);
		
		Member login = memberService.getCheckMemberEmail(member);
		
		if(login != null && "1".equals(login.getMemStatus())) {
			if (!password.equals(login.getMemPassword())) {
				status = "F";
			}
		} else {
			status = "F";
		}
		
		pResponse.setCharacterEncoding("utf-8");

		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(status);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//	@RequestMapping(value = "/forgetPD" , method = {RequestMethod.GET, RequestMethod.POST})
//	public String forgetPD(@ModelAttribute Member member, HttpSession session, Model model) {
//		return "front/member/forgetPD";
//	}

    @ApiIgnore
	@RequestMapping(value = "/setPD" , method = {RequestMethod.GET, RequestMethod.POST})
	public void setPD(HttpServletRequest pRequest, HttpServletResponse pResponse, HttpSession session, Model model) {
		
		String email = pRequest.getParameter("email") != null ? pRequest.getParameter("email") : "";
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<>();
		list.add(map);
		
		Member member = new Member();
		member.setMemEmail(email);
		member = memberService.getCheckMemberEmail(member);
		
		if(member != null) {
			String password = generator(10);
			member.setMemPassword(password);
			try {
				memberService.updatePassword(member);
				String content = "您好,\n您的新密碼為"+password+",\n請登入後立即修改密碼,謝謝。";
				commonService.sendGmail(email, HTML_TITLE+"-忘記密碼", content);

			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			map.put("STATUS", "Y");
			map.put("MSG", "寄送完成");
		} else {
			map.put("STATUS", "N");
			map.put("MSG", "無此帳號");
		}
		
		try {
			JSONArray tJSONArray = new JSONArray(list);
			pResponse.setCharacterEncoding("utf-8");
			PrintWriter out = pResponse.getWriter();
			out.write(tJSONArray.toString());
		} catch(Exception ex) {
			System.out.println("error:"+ex);
		}
		
	}
	
//	@RequestMapping(value = "/forgetPDSuccess" , method = {RequestMethod.GET, RequestMethod.POST})
//	public String forgetPDSuccess(@ModelAttribute Member member, HttpSession session, Model model) {
//		return "front/member/forgetPDSuccess";
//	}
	
	public String generator(int strLen){
		int num = 0;
		String outStr = "";
		while(outStr.length() < strLen) {
			num = (int)(Math.random()*(90-50+1))+50;	//亂數取編號為 50~90 的字符	(排除 0 和 1)
			if (num > 57 && num < 65) {
				continue;			//排除非數字非字母
			} else if (num == 73 || num == 76 || num == 79) {
				continue;			//排除 I、L、O
			}
			outStr += (char)num;
		}
		return outStr.toLowerCase();
	}

}

