package com.holidaydessert.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.holidaydessert.model.MainOrder;
import com.holidaydessert.model.Member;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.MainOrderService;
import com.holidaydessert.service.MemberService;

@RestController
@RequestMapping("/front/member")
public class MemberController {
	
	private final static String HOST = "smtp.gmail.com";
	private final static String AUTH = "true";
//	private final static String PORT = "587";
//	private final static String STARTTLE_ENABLE = "true";
	private final static String SENDER = "s9017611@gmail.com";
	private final static String PASSWORD = "123456";

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MainOrderService mainOrderService;
	
	@Autowired
	private CommonService commonService;
	
	@Value("${html.title}")
	private String htmlTitle;
	
	@RequestMapping(value = "/register" , method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody Member member, HttpSession session) {
		Map<String, Object> responseMap = new HashMap<>();
		try{
			String cKey = "HolidayDessert";
			Calendar date = Calendar.getInstance();
			DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMddHHmmss");
			String yyyymmddStr = yyyymmdd.format(date.getTime());
			String temp = member.getMemEmail() + "," + yyyymmddStr;
			String content = encrypt(temp, cKey);
			String contents = "歡迎您註冊假日甜點,請點擊下列網址以驗證Email信箱:"+"https://www.holidayDessert.com.tw/holidayDessert/member/verificationEmail?code="+URLEncoder.encode( content, "UTF-8" );
			
			Member user = memberService.getCheckMemberEmail(member);
			
			if(user == null) {
				member.setMemAccount(member.getMemEmail());
				member.setMemVerificationStatus("0");
				member.setMemVerificationCode(URLEncoder.encode( content, "UTF-8" ));
				memberService.register(member);
				
				//測試拿掉，上正式要打開
				sendEmail(member.getMemEmail(), URLEncoder.encode( content, "UTF-8" ));
				commonService.sendGmail(member.getMemEmail(), htmlTitle+"-帳戶電子郵件驗證", contents);
				responseMap.put("message", "恭喜你成功註冊為假日甜點會員");
				
			} else {
				responseMap.put("message", "請勿重複註冊");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseMap.put("PATH", "verification.html");
        return ResponseEntity.ok(responseMap);
	}

	@RequestMapping(value = "/verificationEmail" , method = {RequestMethod.POST,RequestMethod.GET})
	public ResponseEntity<?> verificationEmail(HttpSession session, HttpServletRequest pRequest) throws NullPointerException {
		Map<String, Object> responseMap = new HashMap<>();
		String code = pRequest.getParameter("code") == null ? "" : pRequest.getParameter("code");
		String cKey = "HolidayDessert";
		try {
			String content = decrypt(code,cKey);
			String[] text = content.split(",");
			Member member = new Member();
			member.setMemEmail(text[0]);
			
			member = memberService.getCheckMemberEmail(member);
			memberService.verificationEmail(member);
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		responseMap.put("PATH", "/holidayDessert/member/verificationSuccess.html");
		
        return ResponseEntity.ok(responseMap);
	}
	
	@RequestMapping(value = "/checkMemberAccountEmail", method = RequestMethod.POST)
	public ResponseEntity<?> checkMemberAccountEmail(@RequestBody Member member) {
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
	
	@RequestMapping(value = "/verificationSuccess" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verificationSuccess(@SessionAttribute("memberSession") Member memberSession, HttpSession session, Model model, HttpServletRequest pRequest) {
		return "front/member/verificationSuccess";
	}
	
	@RequestMapping(value = "/verification" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verification(@SessionAttribute("memberSession") Member memberSession, HttpSession session, Model model, HttpServletRequest pRequest) {
		return "front/member/verification";
	}

	@RequestMapping(value = "/reSendEmail", method = { RequestMethod.POST, RequestMethod.GET })
	public void reSendEmail(HttpServletRequest pRequest, HttpServletResponse pResponse) {
		String email = pRequest.getParameter("email") == null ? "" : pRequest.getParameter("email");
		String cKey = "HolidayDessert";
		String result = "驗證信寄出異常";
		try {
			Member member = new Member();
			member.setMemEmail(email);
			member = memberService.getCheckMemberEmail(member);

			Calendar date = Calendar.getInstance();
			DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMddHHmmss");
			String yyyymmddStr = yyyymmdd.format(date.getTime());
			String temp = email + "," + yyyymmddStr;
			String content = encrypt(temp, cKey);
			String contents = "歡迎您註冊假日甜點,請點擊下列網址以驗證Email信箱:"
					+ "https://www.holidayDessert.com.tw/holidayDessert/member/verificationEmail?code="
					+ URLEncoder.encode(content, "UTF-8");

			if (member != null) {
				if ("1".equals(member.getMemVerificationStatus())) {
					result = "信箱已驗證過";
				} else {
					member.setMemVerificationCode(URLEncoder.encode(content, "UTF-8"));
					memberService.register(member);

					// 測試拿掉，上正式要打開
					sendEmail(member.getMemEmail(), URLEncoder.encode(content, "UTF-8"));
					commonService.sendGmail(member.getMemEmail(), htmlTitle + "-帳戶電子郵件驗證", contents);
					memberService.updateVerification(member);

					result = "驗證信已寄出";
				}

			}

		} catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pResponse.setCharacterEncoding("utf-8");
		try {
			PrintWriter out = pResponse.getWriter();
			out.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void sendEmail(String email, String content) {

		try {
			Properties properties = System.getProperties();// 獲取系統屬性

			properties.setProperty("mail.transport.protocol", "smtp");
			properties.setProperty("mail.smtp.host", HOST);// 設置郵件服務器
			properties.setProperty("mail.smtp.auth", AUTH);// 打開認證

			Session mailSession = Session.getDefaultInstance(properties, null);
			InternetAddress from = new InternetAddress("s9017611@gmail.com");
			// 產生整封 email 的主體 message
			MimeMessage msg = new MimeMessage(mailSession);

			msg.setSubject("假日甜點系統 帳戶電子郵件驗證");
			msg.setFrom(from);
			msg.setRecipients(Message.RecipientType.TO, email);
			msg.setText("歡迎您註冊假日甜點,請點擊下列網址以驗證Email信箱:"
					+ "https://www.holidayDessert.com.tw/holidayDessert/member/verificationEmail?code=" + content);

			Transport transport = mailSession.getTransport("smtp");
			transport.connect(HOST, SENDER, PASSWORD);
			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static String encrypt(String sSrc, String sKey) throws Exception {
		Base64.Encoder encoder = Base64.getEncoder();
		if (sKey == null) {
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			return null;
		}
		byte[] raw = sKey.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());

		return encoder.encodeToString(encrypted);
	}

	// 解密
	public static String decrypt(String sSrc, String sKey) throws Exception {
		Base64.Decoder decoder = Base64.getDecoder();
//		new String(decoder.decode(sSrc);
		try {
			// 判断Key是否正确
			if (sKey == null) {
				System.out.print("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				System.out.print("Key长度不是16位");
				return null;
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
			byte[] encrypted1 = decoder.decode(sSrc);// 先用base64解密
//			System.out.println("encrypted1: "+encrypted1.toString());
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}
	
	@RequestMapping(value = "/update" , method = {RequestMethod.GET, RequestMethod.POST} , produces="text/plain;charset=UTF-8")
	public String update(@SessionAttribute("memberSession") Member memberSession, @ModelAttribute Member member, HttpSession session, Model model) {
		
		try {
			if(memberSession == null) {
				model.addAttribute("PATH", "/holidayDessert/index"); 
				return "front/toPath";
			}
			// 取得舊資料
			Member user = new Member();
			user = memberService.getCheckMemberEmail(memberSession);
			model.addAttribute("member", user);
			
			MainOrder order = new MainOrder();
			order.setMemId(user.getMemId());
			List<Map<String, Object>> orderChildList = mainOrderService.getMemOrderList(order);
			model.addAttribute("orderChildList", orderChildList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "front/member/update";
	}
	
	@RequestMapping(value = "/updateSubmit" , method = RequestMethod.POST , produces="text/plain;charset=UTF-8")
	public String updateSubmit(@SessionAttribute("memberSession") Member memberSession, @ModelAttribute Member member, HttpSession session, Model model, HttpServletRequest pRequest) {
		
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
	
	@RequestMapping(value = "/updatePassword" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePassword(@SessionAttribute("memberSession") Member memberSession, @ModelAttribute Member member, HttpSession session, Model model) {
		return "front/member/updatePassword";
	}

	@RequestMapping(value = "/updatePasswordSubmit" , method = RequestMethod.POST)
	public String updatePasswordSubmit(@SessionAttribute("memberSession") Member memberSession, @ModelAttribute Member member, HttpSession session, Model model, HttpServletRequest pRequest) {
		
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
	
	@RequestMapping(value = "/checkPassword" , method = {RequestMethod.GET, RequestMethod.POST})
	public void checkPassword(@SessionAttribute("memberSession") Member memberSession, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {

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
	
	@RequestMapping(value = "/forgetPD" , method = {RequestMethod.GET, RequestMethod.POST})
	public String forgetPD(@ModelAttribute Member member, HttpSession session, Model model) {
		return "front/member/forgetPD";
	}
	
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
				commonService.sendGmail(email, htmlTitle+"-忘記密碼", content);

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
	
	@RequestMapping(value = "/forgetPDSuccess" , method = {RequestMethod.GET, RequestMethod.POST})
	public String forgetPDSuccess(@ModelAttribute Member member, HttpSession session, Model model) {
		return "front/member/forgetPDSuccess";
	}
	
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

