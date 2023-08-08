package com.holidaydessert.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
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
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.holidaydessert.model.Member;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.MemberService;

@Controller
@SessionAttributes("memberSession")
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CommonService commonService;
	
	@Value("${html.title}")
	private String htmlTitle;

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
	
	@RequestMapping(value = "/register" , method = {RequestMethod.GET, RequestMethod.POST})
	public String register(@ModelAttribute Member member, HttpSession session, Model model, HttpServletRequest pRequest) {

		try {
			Member memberSession = (Member)session.getAttribute("memberSession");
			String name = pRequest.getParameter("NAME") == null ? "" : pRequest.getParameter("NAME");
			String fb_uid = pRequest.getParameter("FB_UID") == null ? "" : pRequest.getParameter("FB_UID");
			String email = pRequest.getParameter("EMAIL") == null ? "" : pRequest.getParameter("EMAIL");
			
			member.setMemName(name);
			member.setMemAccount(fb_uid);
			member.setMemEmail(email);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "front/member/register";
	}
	
	@RequestMapping(value = "/addSubmit" , method = RequestMethod.POST)
	public String registerAddSubmit(@ModelAttribute Member member, HttpSession session, Model model) {
		
		try{
			String cKey = "HolidayDessert";
			Calendar date = Calendar.getInstance();
			DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMddHHmmss");
			String yyyymmddStr = yyyymmdd.format(date.getTime());
			String temp = member.getMemEmail() + "," + yyyymmddStr;
			String content = encrypt(temp, cKey);
			String contents = "歡迎您註冊假日甜點,請點擊下列網址以驗證Email信箱:"+"https://www.reallygood.com.tw/foreast/member/verificationEmail?code="+URLEncoder.encode( content, "UTF-8" );
			
			Member user = memberService.getCheckMemberEmail(member);
			
			if(user == null) {
				
				member.setMemVerificationCode(URLEncoder.encode( content, "UTF-8" ));
				memberService.register(member);
				
				//測試拿掉，上正式要打開
				sendEmail(member.getMemEmail(), URLEncoder.encode( content, "UTF-8" ));
				commonService.sendGmail(member.getMemEmail(), htmlTitle+"-帳戶電子郵件驗證", contents);
				
				model.addAttribute("message", "恭喜你成功註冊為本網站會員");
				
			} else {
				model.addAttribute("message", "請勿重複註冊");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("PATH", "verification");
		return "front/path";
	}
	
	@RequestMapping(value = "/verificationEmail" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verificationEmail(HttpSession session, Model model, HttpServletRequest pRequest) throws NullPointerException {
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
		
		model.addAttribute("PATH", "/forecast/member/verificationSuccess");
		return "front/toPath";
	}

	@RequestMapping(value = "/verificationSuccess" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verificationSuccess(HttpSession session, Model model, HttpServletRequest pRequest) {
		//顯示12志願數量
		int wishCount = 0;
		Member memberSession = (Member)session.getAttribute("memberSession");
		if(memberSession != null) {
			int year = yearUsePeriodService.getYear();
			MemberWish memberWish = new MemberWish();
			memberWish.setExam_number_id(memberSession.getExam_number_id());
			memberWish.setWish_type("12");
			memberWish.setYear(String.valueOf(year));
			wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
		}
		model.addAttribute("WISH_COUNT", wishCount);
		
		//廣告
		Banner banner = new Banner();
		banner.setBanner_type("B");
		List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
		model.addAttribute("bottomList", bottomList);
		return "front/member/verificationSuccess";
	}
	
	@RequestMapping(value = "/verification" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verification(HttpSession session, Model model, HttpServletRequest pRequest) {
		
		//顯示12志願數量
		int wishCount = 0;
		MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
		if(memberAccountSession != null) {
			int year = yearUsePeriodService.getYear();
			MemberWish memberWish = new MemberWish();
			memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
			memberWish.setWish_type("12");
			memberWish.setYear(String.valueOf(year));
			wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
		}
		model.addAttribute("WISH_COUNT", wishCount);
		
		//廣告
		Banner banner = new Banner();
		banner.setBanner_type("B");
		List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
//		banner.setBanner_type("A");
//		List<Map<String, Object>> rightList = bannerService.frontRandList(banner);
		model.addAttribute("bottomList", bottomList);
//		model.addAttribute("rightList", rightList);
		
		return "front/member/verification";
	}
	
	@RequestMapping(value = "/verificationSNSuccess" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verificationSNSuccess(HttpSession session, Model model, HttpServletRequest pRequest) {
		//顯示12志願數量
		int wishCount = 0;
		MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
		if(memberAccountSession != null) {
			int year = yearUsePeriodService.getYear();
			MemberWish memberWish = new MemberWish();
			memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
			memberWish.setWish_type("12");
			memberWish.setYear(String.valueOf(year));
			wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
		}
		model.addAttribute("WISH_COUNT", wishCount);
		
		//廣告
		Banner banner = new Banner();
		banner.setBanner_type("B");
		List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
		model.addAttribute("bottomList", bottomList);
		return "front/member/verificationSNSuccess";
	}
	
	@RequestMapping(value = "/verificationSN" , method = {RequestMethod.POST,RequestMethod.GET})
	public String verificationSN(HttpSession session, Model model, HttpServletRequest pRequest) {
		
		//顯示12志願數量
		int wishCount = 0;
		MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
		if(memberAccountSession != null) {
			int year = yearUsePeriodService.getYear();
			MemberWish memberWish = new MemberWish();
			memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
			memberWish.setWish_type("12");
			memberWish.setYear(String.valueOf(year));
			wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
		}
		model.addAttribute("WISH_COUNT", wishCount);
		
		//廣告
		Banner banner = new Banner();
		banner.setBanner_type("B");
		List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
//		banner.setBanner_type("A");
//		List<Map<String, Object>> rightList = bannerService.frontRandList(banner);
		model.addAttribute("bottomList", bottomList);
//		model.addAttribute("rightList", rightList);
		
		return "front/member/verificationSN";
	}
	
	@RequestMapping(value = "/reSendEmail" , method = {RequestMethod.POST,RequestMethod.GET})
	public void reSendEmail( HttpSession session, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		String email = pRequest.getParameter("email") == null ? "" : pRequest.getParameter("email");
		String cKey = "tkbs_tkbt_foreca";
		String result = "驗證信寄出異常";
		try {
			MemberAccount memberAccount = new MemberAccount();
			memberAccount.setEmail(email);
			memberAccount = memberAccountService.data(memberAccount);
			
			if(memberAccount != null) {
				if(Integer.valueOf(memberAccount.getVerification_count()) >= 3) {
					result = "超出寄信額度,寄驗證信次數至多三次";
				}else if("1".equals(memberAccount.getVerification_status())){
					result = "信箱已驗證過";
				}else {
					Calendar date = Calendar.getInstance();
					DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMddHHmmss");
					String yyyymmddStr = yyyymmdd.format(date.getTime());
					String temp = email + "," + yyyymmddStr;
					String content = encrypt(temp, cKey);
					memberAccount.setVerification_code(URLEncoder.encode( content, "UTF-8" ));
					
					memberAccountService.updateVerification(memberAccount);
					String contents = "歡迎您註冊落點分析,請點擊下列網址以驗證Email信箱:"+"https://www.reallygood.com.tw/forecast/member/verificationEmail?code="+URLEncoder.encode( content, "UTF-8" );
//					sendEmail(memberAccount.getEmail(), URLEncoder.encode( content, "UTF-8" ));
					commonService.sendGmail(memberAccount.getEmail(), htmlTitle+"-帳戶電子郵件驗證", contents);
					
					result = "驗證信已寄出";
				}
				
				
			}

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		pResponse.setCharacterEncoding("utf-8");

		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendEmail(String email, String content) {
		 	
	        try {
	        	Properties properties = System.getProperties();// 獲取系統屬性
			 	
			 	properties.setProperty("mail.transport.protocol", "smtp");
		        properties.setProperty("mail.smtp.host", "mail.tkbnews.com.tw");// 設置郵件服務器
		        properties.setProperty("mail.smtp.auth", "true");// 打開認證
//		        properties.setProperty("mail.user", "ifUserNameNeeded");
//		        properties.setProperty("mail.password", "ifPasswordNeeded");
		        
		        Session mailSession = Session.getDefaultInstance(properties, null);
//	            Transport transport = mailSession.getTransport();
	            InternetAddress from = new InternetAddress("service@tkbnews.com.tw");
	            // 產生整封 email 的主體 message
	            MimeMessage msg = new MimeMessage(mailSession);
	            
	            msg.setSubject("甄戰即時落點系統 帳戶電子郵件驗證");
	            msg.setFrom(from);
	    		msg.setRecipients(Message.RecipientType.TO, email);
	    		msg.setText("歡迎您註冊落點分析,請點擊下列網址以驗證Email信箱:"+"https://www.reallygood.com.tw/forecast/member/verificationEmail?code="+content);
	    		
//	            msg.setSubject("Welcome to TKB Forecast System - Verify Your Email Address");
//	            msg.setFrom(from);
//	    		msg.setRecipients(Message.RecipientType.TO, email);
//	    		msg.setText("Please use the link below to verify your email address.\n\n"+"Link : https://www.reallygood.com.tw/forecast/member/verificationEmail?code="+content+"\n\nBy Taiwan Knowledge Bank");
	            
//	            //QQ郵箱需要下面這段代碼，163郵箱不需要
//	            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//	            sf.setTrustAllHosts(true);
//	            properties.put("mail.smtp.ssl.enable", "true");
//	            properties.put("mail.smtp.ssl.socketFactory", sf);

	        	Transport transport = mailSession.getTransport("smtp");
	        	transport.connect("mail.tkbnews.com.tw", "byone@tkbnews.com.tw", "j;6m06vu06");
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
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
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
			IvParameterSpec iv = new IvParameterSpec("0102030405060708"
			.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
			byte[] encrypted1 = decoder.decode(sSrc);//先用base64解密
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
	public String update(
			@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, 
			@ModelAttribute MemberAccount memberAccount, 
			HttpSession session, 
			Model model) {
		
		try {
			if(memberAccountSession == null) {
				model.addAttribute("PATH", "/forecast/index"); 
				return "front/toPath";
			}
			
			//顯示12志願數量
			int wishCount = 0;
			int year = yearUsePeriodService.getYear();
			if(memberAccountSession != null) {
				MemberWish memberWish = new MemberWish();
				memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
				memberWish.setWish_type("12");
				memberWish.setYear(String.valueOf(year));
				wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
			}
			model.addAttribute("WISH_COUNT", wishCount);

			// 取得基本資料
			Map member = memberAPIService.getData(memberAccountSession);
			
			MemberAccount memberAccount_new = new MemberAccount();
			memberAccount_new = memberAccountService.data(memberAccountSession);
			memberAccount_new.setChinese_name(memberAccount_new.getName());
			memberAccount_new.setGender(member.get("GENDER")!=null ? member.get("GENDER").toString() : "");
			
			if(member.get("ID_NO") != null) memberAccount_new.setId_no(String.valueOf(member.get("ID_NO")));
			
			memberAccount_new.setEmail(member.get("EMAIL").toString());
			memberAccount_new.setPhone(member.get("PHONE").toString());
			memberAccount_new.setCellphone(member.get("PHONE").toString());
			memberAccount_new.setPassword(member.get("PASSWORD").toString());
			memberAccount_new.setApprove(Integer.valueOf(member.get("APPROVE").toString()));
			memberAccount_new.setMember_id(Integer.valueOf(member.get("ID").toString()));
			model.addAttribute("memberAccount", memberAccount_new);

			// 取得准考證
			MemberDetail memberDetail = new MemberDetail();
			memberDetail.setYear(String.valueOf(year));
			memberDetail.setMember_id(memberAccountSession.getId());
			model.addAttribute("memberDetail", memberDetail);
			
			List<Map<String, Object>> memberDetailList = memberDetailService.presetList(memberDetail);
			model.addAttribute("memberDetailList", memberDetailList);
			
			// 取得志願序號
			Authorization authorization = new Authorization();
			authorization.setYear(String.valueOf(year));
			authorization.setMember_id(memberAccountSession.getId());
			authorization.setUse_by(memberAccountSession.getEmail());
			authorization.setCreate_by(memberAccountSession.getEmail());
			model.addAttribute("authorization", authorization);
			
			List<Map<String, Object>> list = authorizationService.getFrontList(authorization);
			model.addAttribute("list", list);
			
			List<Map<String, Object>> authCreateByList = authorizationService.getCreateByList(authorization);
			model.addAttribute("authCreateByList", authCreateByList);
			
			// 取得訂單資訊
			Order order = new Order();
			order.setEmail(memberAccount_new.getEmail());
			List<Map<String, Object>> orderParentList = orderService.getOrderListAndNumberStatusParent(order);
			if(orderParentList != null) {
				for(int i=0; i<orderParentList.size(); i++) {
					InvoiceMaster invoiceMaster = new InvoiceMaster();
					invoiceMaster.setInvoice_no(orderParentList.get(i).get("INVOICE_NO").toString());
					InvoiceMaster getInvoiceMaster = invoiceService.getInvoiceMaster(invoiceMaster);
					if(getInvoiceMaster != null) {
						orderParentList.get(i).put("GUI_NO", getInvoiceMaster.getGui_no());
					}
				}
			}
			model.addAttribute("orderParentList", orderParentList);
			
			List<Map<String, Object>> orderChildList = orderService.getOrderListAndNumberStatusChild(order);
			model.addAttribute("orderChildList", orderChildList);
			
			MemberWish memberWish = new MemberWish();
			memberWish.setYear(String.valueOf(year));
			memberWish.setWish_type("12");
			memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
			List<Map<String, Object>> wishList = memberWishService.list(memberWish);
			model.addAttribute("wishList", wishList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "front/member/update";
		
	}
	
	@RequestMapping(value = "/updateSubmit" , method = RequestMethod.POST , produces="text/plain;charset=UTF-8")
	public String updateSubmit(@ModelAttribute MemberAccount memberAccount, HttpSession session, Model model, HttpServletRequest pRequest) {
		MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
		Map map = new HashMap();
		memberAccount.setUpdate_by(memberAccountSession.getEmail());
		memberAccount.setEmail(memberAccountSession.getEmail());

		memberAccount.setName(memberAccount.getChinese_name());

		try{
			
//			System.out.println("Chinese_name:"+URLDecoder.decode(URLEncoder.encode(memberAccount.getChinese_name(), "UTF-8"), "UTF-8"));
//			System.out.println("Name:"+memberAccount.getName());
			memberAccountService.frontUpdate(memberAccount);
			map = memberAPIService.update(memberAccount);
			model.addAttribute("message", " 修改會員資料成功");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

//		return register(memberAccount, session, model);
		model.addAttribute("PATH", "/forecast/member/update");
		return "front/toPath";
	}
	
	@RequestMapping(value = "/getScore" , method = {RequestMethod.GET, RequestMethod.POST})
	public void getScore(HttpSession session, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		
		String exam_id = pRequest.getParameter("exam_id") == null ? "" : pRequest.getParameter("exam_id");
		
		List<MemberDetail> list = new ArrayList<MemberDetail>();
		try {
			
			MemberDetail memberDetail = new MemberDetail();
			memberDetail.setId(exam_id);
			memberDetail = memberDetailService.dataById(memberDetail);
			list.add(memberDetail);
//			model.addAttribute("memberDetail", memberDetail);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		JSONArray tJSONArray = new JSONArray(list);
		pResponse.setCharacterEncoding("utf-8");

		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(tJSONArray.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/updateScoreSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateScoreSubmit(@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, @ModelAttribute MemberDetail memberDetail, HttpSession session, Model model, HttpServletRequest pRequest) {
		
		try {
			
			memberDetail.setUpdate_by(memberAccountSession.getEmail());

			memberDetailService.update(memberDetail);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("PATH", "/forecast/member/update");
		return "front/toPath";
	}
	
	@RequestMapping(value = "/examNumData" , method = {RequestMethod.GET, RequestMethod.POST})
	public String examNumData(@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, HttpSession session, Model model) {
		
		try {

			MemberDetail memberDetail = new MemberDetail();
			memberDetail.setMember_id(memberAccountSession.getId());
			List<Map<String, Object>> memberDetailList = memberDetailService.list(memberDetail);

			model.addAttribute("memberDetailList", memberDetailList);
			model.addAttribute("memberDetail", memberDetail);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "front/member/examNum";
	}
	
	@RequestMapping(value = "/addExamNum" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addExamNum(@SessionAttribute("memberSession") MemberAccount memberAccountSession, @ModelAttribute MemberDetail memberDetail, HttpSession session, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		try {
			int year = yearUsePeriodService.getYear();
			
			memberDetail.setYear(String.valueOf(year));
			memberDetail.setMember_id(memberAccountSession.getId());
			memberDetail.setCreate_by(memberAccountSession.getEmail());
			memberDetail.setUpdate_by(memberAccountSession.getEmail());
			MemberDetail memberDetailByMember = memberDetailService.dataByMemberAndNum(memberDetail);
			//檢查准考證是否重複輸入
			if(memberDetailByMember != null) {
				model.addAttribute("message", "新增失敗,重複輸入相同准考證");
			}else {
				
				memberDetail.setYear(String.valueOf(year));

				//如果出現重複准考證卻未輸入身份證號碼,將狀態改成B
				if("A".equals(memberDetail.getStatus()) || "B".equals(memberDetail.getStatus())) {
					if(memberDetail.getId_no() == null || "".equals(memberDetail.getId_no())) {
						memberDetail.setStatus("B");
					}
				}
				
				int detail_id = memberDetailService.add(memberDetail);
				memberDetail.setId(String.valueOf(detail_id));
				
				if("A".equals(memberDetail.getStatus()) || "B".equals(memberDetail.getStatus()) || "F".equals(memberDetail.getStatus())) {
					MemberAccount maccount = new MemberAccount();
					maccount.setId(memberAccountSession.getId());
				}
				
				//寫入紅利時間
				if("A".equals(memberDetail.getStatus()) || "B".equals(memberDetail.getStatus())) {
					memberDetailService.updatePunishmentTime(memberDetail);
					memberDetailService.setStatusIsA(memberDetail);
				}
				
				if(Integer.valueOf(memberAccountSession.getLevel()) > 2 && !"A".equals(memberDetail.getStatus()) && !"B".equals(memberDetail.getStatus()) && !"F".equals(memberDetail.getStatus())) {
					//更新帳號權限
					Identity identity = new Identity();
					identity.setName("前台");
					identity.setLevel("2");
					int identity_id = identityService.getFrontIdentity(identity);
					MemberAccount memberAccount = new MemberAccount();
					memberAccount.setIdentity_id(String.valueOf(identity_id));
					memberAccount.setId(memberAccountSession.getId());
					memberAccountService.updateIdentity(memberAccount);

					memberAccountSession.setLevel("2");
					memberAccountSession.setIdentity_id(String.valueOf(identity_id));
					
					//若為空,設定預設准考證號碼
					if(memberAccountSession.getExam_number() == null && memberAccountSession.getExam_number_id() == null) {
						memberAccount.setExam_number(memberDetail.getExam_number());
						memberAccount.setExam_number_id(memberDetail.getId());
						memberAccountService.updateExamNum(memberAccount);
						memberAccountSession.setExam_number(memberDetail.getExam_number());
						memberAccountSession.setExam_number_id(memberDetail.getId());
						
						//cookie紀錄國文、英文、數學、自然、社會、英聽、APCS觀念、APCS實作
						
						FractionConversion fractionConversion = new FractionConversion();
						fractionConversion.setYear(String.valueOf(year));
						fractionConversion.setSubject("國文");
						fractionConversion.setScore(memberDetail.getChinese());
						FractionConversion cc = fractionConversionService.conversion(fractionConversion);
						fractionConversion.setSubject("英文");
						fractionConversion.setScore(memberDetail.getEnglish());
						FractionConversion ec = fractionConversionService.conversion(fractionConversion);
						fractionConversion.setSubject("數學");
						fractionConversion.setScore(memberDetail.getMathematics());
						FractionConversion mc = fractionConversionService.conversion(fractionConversion);
						fractionConversion.setSubject("數學");
						fractionConversion.setScore(memberDetail.getMathematics_a());
						FractionConversion mac = fractionConversionService.conversion(fractionConversion);
						fractionConversion.setSubject("數學");
						fractionConversion.setScore(memberDetail.getMathematics_b());
						FractionConversion mbc = fractionConversionService.conversion(fractionConversion);
						fractionConversion.setSubject("社會");
						fractionConversion.setScore(memberDetail.getSociety());
						FractionConversion sc = fractionConversionService.conversion(fractionConversion);
						fractionConversion.setSubject("自然");
						fractionConversion.setScore(memberDetail.getNaturally());
						FractionConversion nc = fractionConversionService.conversion(fractionConversion);
						
						ResponseCookieUtil.addCookie("c", memberDetail.getChinese(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("e", memberDetail.getEnglish(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("m", memberDetail.getMathematics(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("ma", memberDetail.getMathematics_a(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("mb", memberDetail.getMathematics_b(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("s", memberDetail.getSociety(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("n", memberDetail.getNaturally(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("eh", memberDetail.getEnglish_hearing(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("ac", memberDetail.getApcs_concept(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("ap", memberDetail.getApcs_practice(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("c_c", cc!=null ? cc.getOne_years_ago_score() : memberDetail.getChinese(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("e_c", ec!=null ? ec.getOne_years_ago_score() : memberDetail.getEnglish(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("m_c", mc!=null ? mc.getOne_years_ago_score() : memberDetail.getMathematics(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("ma_c", mac!=null ? mac.getOne_years_ago_score() : memberDetail.getMathematics_a(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("mb_c", mbc!=null ? mbc.getOne_years_ago_score() : memberDetail.getMathematics_b(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("s_c", sc!=null ? sc.getOne_years_ago_score() : memberDetail.getSociety(), false, true, "/forecast", -1, "None", pResponse);
						ResponseCookieUtil.addCookie("n_c", nc!=null ? nc.getOne_years_ago_score() : memberDetail.getNaturally(), false, true, "/forecast", -1, "None", pResponse);
					}
					
				}
				if ("".equals(memberDetail.getSchoolDept()) != true && "".equals(memberDetail.getSex()) != true) {
					
					String school_dept_no = memberDetail.getSchoolDept();
					String sex = memberDetail.getSex();
					SchoolDepartment schoolDepartment = new SchoolDepartment();
					schoolDepartment.setSchool_dept_no(school_dept_no);
					schoolDepartment.setSex(sex);
					schoolDepartment.setYear(String.valueOf(year));
					schoolDepartment = schoolDepartmentService.getDataBySchoolDeptNoSex(schoolDepartment, "all");
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = new HashMap<String, Object>();
					list.add(map);
					
					MemberWish memberWish = new MemberWish();
					memberWish.setYear(String.valueOf(year));
					memberWish.setExam_number(memberAccountSession.getExam_number());
					memberWish.setExam_number_id(memberDetail.getId());
					memberWish.setSchool_department_id(schoolDepartment.getId());
					memberWish.setSchool_dept_no(school_dept_no);
					memberWish.setEighteen_group_id(schoolDepartment.getEighteen_group_id());
					memberWish.setEighteen_group_name(schoolDepartment.getEighteen_group_name());
					memberWish.setWish_type("12");
					memberWish.setSchool(schoolDepartment.getSchool_name());
					memberWish.setDepartment(schoolDepartment.getDepartment_name());
					memberWish.setSchool_area(schoolDepartment.getSchool_area());
					memberWish.setSchool_type(schoolDepartment.getSchool_type());
					memberWish.setCreate_by(memberAccountSession.getEmail());
					memberWish.setUpdate_by(memberAccountSession.getEmail());
					memberWish.setSex(schoolDepartment.getSex());
					
					int count = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
					
					if((count+1) > 12) {
						map.put("STATUS", "N");
						map.put("MSG", "已選擇超過12校系，請重新確認");
					} else {
						map.put("STATUS", "Y");
						map.put("COUNT", count+1);
						memberWishService.add(memberWish);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if ("".equals(memberDetail.getSchoolDept()) != true && "".equals(memberDetail.getSex()) != true) {
			model.addAttribute("PATH", "/forecast/wish/12/select");
		} else if(pRequest.getParameter("jude_guessyoulike") != null && pRequest.getParameter("jude_guessyoulike").equals("jude_guessyoulike")) {
			model.addAttribute("PATH", "/forecast/university/select");
		} else {
			model.addAttribute("PATH", "/forecast/member/update");
		}
		return "front/toPath";
	}
	
	@RequestMapping(value = "/updateMemberExamNum" , method = {RequestMethod.POST,RequestMethod.GET})
	public void updateMemberExamNum(@SessionAttribute("memberSession") MemberAccount memberAccountSession, HttpSession session, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) {
		String exam_num_id = pRequest.getParameter("exam_num_id") == null ? "" : pRequest.getParameter("exam_num_id");
		String exam_num = pRequest.getParameter("exam_num") == null ? "" : pRequest.getParameter("exam_num");
		String status = pRequest.getParameter("status") == null ? "" : pRequest.getParameter("status");
		String id_no = pRequest.getParameter("id_no") == null ? "" : pRequest.getParameter("id_no");
		String result = "F";
		try {
			
			MemberAccount memberAccount = new MemberAccount();
			memberAccount.setId(memberAccountSession.getId());
			memberAccount.setExam_number(exam_num);
			memberAccount.setExam_number_id(exam_num_id);
			memberAccountService.updateExamNum(memberAccount);
			memberAccountSession.setExam_number(exam_num);
			memberAccountSession.setExam_number_id(exam_num_id);
			result = "T";
			
			if(Integer.valueOf(memberAccountSession.getLevel()) <= 2) {
				
				int year = yearUsePeriodService.getYear();
				
				//取得6志願列表
				MemberWish mw6 = new MemberWish();
				mw6.setWish_type("6");
				mw6.setExam_number_id(memberAccountSession.getExam_number_id());
				mw6.setYear(String.valueOf(year));
				List<Map<String, Object>> wish6 = memberWishService.getAuthList(mw6);
				MemberAccount ma6 = new MemberAccount();
				if(wish6!=null && wish6.size()>0) {
					ma6.setId(memberAccountSession.getId());
					ma6.setAuthorization_id(wish6.get(0).get("AUTHORIZATION_ID").toString());
				} else {
					ma6.setId(memberAccountSession.getId());
					ma6.setAuthorization_id(null);
				}
				memberAccountService.updateAuthNum(ma6);
				memberAccountSession.setAuthorization_id(ma6.getAuthorization_id());
				
				//取得5志願列表
				MemberWish mw5 = new MemberWish();
				mw5.setWish_type("5");
				mw5.setExam_number_id(memberAccountSession.getExam_number_id());
				mw5.setYear(String.valueOf(year));
				List<Map<String, Object>> wish5 = memberWishService.getAuthList(mw5);
				MemberAccount ma5 = new MemberAccount();
				if(wish5!=null && wish5.size()>0) {
					ma5.setId(memberAccountSession.getId());
					ma5.setAuthorization_id2(wish5.get(0).get("AUTHORIZATION_ID").toString());
				} else {
					ma5.setId(memberAccountSession.getId());
					ma5.setAuthorization_id2(null);
				}
				memberAccountService.updateAuthNum2(ma5);
				memberAccountSession.setAuthorization_id2(ma5.getAuthorization_id2());
				
				if(memberAccountSession.getAuthorization_id()==null && memberAccountSession.getAuthorization_id2()==null) {
					//降等至追蹤校系會員
					Identity identity = new Identity();
					identity.setName("前台");
					identity.setLevel("2");
					int identity_id = identityService.getFrontIdentity(identity);
					MemberAccount memberAccount2 = new MemberAccount();
					memberAccount2.setId(memberAccountSession.getId());
					memberAccount2.setExam_number(exam_num);
					memberAccount2.setExam_number_id(exam_num_id);
					memberAccount2.setIdentity_id(String.valueOf(identity_id));
					memberAccount2.setId(memberAccountSession.getId());
					memberAccountService.updateIdentity(memberAccount2);
					
					memberAccountSession.setLevel("2");
					memberAccountSession.setIdentity_id(String.valueOf(identity_id));
				} else {
					//升等至志願會員
					Identity identity = new Identity();
					identity.setName("前台");
					identity.setLevel("1");
					int identity_id = identityService.getFrontIdentity(identity);
					MemberAccount memberAccount2 = new MemberAccount();
					memberAccount2.setId(memberAccountSession.getId());
					memberAccount2.setExam_number(exam_num);
					memberAccount2.setExam_number_id(exam_num_id);
					memberAccount2.setIdentity_id(String.valueOf(identity_id));
					memberAccount2.setId(memberAccountSession.getId());
					memberAccountService.updateIdentity(memberAccount2);
					
					memberAccountSession.setLevel("1");
					memberAccountSession.setIdentity_id(String.valueOf(identity_id));
				}
				
			}
			
			if(Integer.valueOf(memberAccountSession.getLevel()) > 2) {
				
				//更新帳號權限
				Identity identity = new Identity();
				identity.setName("前台");
				identity.setLevel("2");
				int identity_id = identityService.getFrontIdentity(identity);
				MemberAccount memberAccount2 = new MemberAccount();
				memberAccount2.setId(memberAccountSession.getId());
				memberAccount2.setExam_number(exam_num);
				memberAccount2.setExam_number_id(exam_num_id);
				memberAccount2.setIdentity_id(String.valueOf(identity_id));
				memberAccount2.setId(memberAccountSession.getId());
				memberAccountService.updateIdentity(memberAccount2);
				
				memberAccountSession.setLevel("2");
				memberAccountSession.setIdentity_id(String.valueOf(identity_id));
				
				//若為空,設定預設准考證號碼 
				if(memberAccountSession.getExam_number() == null && memberAccountSession.getExam_number_id() == null) { 
					memberAccount2.setExam_number(exam_num); 
					memberAccount2.setExam_number_id(exam_num_id); 
					memberAccountService.updateExamNum(memberAccount2); 
					memberAccountSession.setExam_number(exam_num); 
					memberAccountSession.setExam_number_id(exam_num_id); 
				}

			}
			
			memberAccount.setAuthorization_id(null);
			memberAccountService.updateAuthNum(memberAccount);
			memberAccountSession.setAuthorization_id(null);
			
			MemberDetail memberDetail = new MemberDetail();
			
			if(!"".equals(exam_num_id) && !"".equals(status) && !"".equals(id_no)) {
				memberDetail.setId(exam_num_id);
				memberDetail.setStatus(status);
				memberDetail.setId_no(id_no);
				memberDetailService.updateIdNoPunishmentTimeByMember(memberDetail);
			}
			
			memberDetail = new MemberDetail();
			memberDetail.setId(exam_num_id);
			memberDetail = memberDetailService.dataById(memberDetail);
			
			//cookie紀錄國文、英文、數學、自然、社會、英聽、APCS觀念、APCS實作
			
			int year = yearUsePeriodService.getYear();
			FractionConversion fractionConversion = new FractionConversion();
			fractionConversion.setYear(String.valueOf(year));
			fractionConversion.setSubject("國文");
			fractionConversion.setScore(memberDetail.getChinese());
			FractionConversion cc = fractionConversionService.conversion(fractionConversion);
			fractionConversion.setSubject("英文");
			fractionConversion.setScore(memberDetail.getEnglish());
			FractionConversion ec = fractionConversionService.conversion(fractionConversion);
			fractionConversion.setSubject("數學");
			fractionConversion.setScore(memberDetail.getMathematics());
			FractionConversion mc = fractionConversionService.conversion(fractionConversion);
			fractionConversion.setSubject("數學");
			fractionConversion.setScore(memberDetail.getMathematics_a());
			FractionConversion mac = fractionConversionService.conversion(fractionConversion);
			fractionConversion.setSubject("數學");
			fractionConversion.setScore(memberDetail.getMathematics_b());
			FractionConversion mbc = fractionConversionService.conversion(fractionConversion);
			fractionConversion.setSubject("社會");
			fractionConversion.setScore(memberDetail.getSociety());
			FractionConversion sc = fractionConversionService.conversion(fractionConversion);
			fractionConversion.setSubject("自然");
			fractionConversion.setScore(memberDetail.getNaturally());
			FractionConversion nc = fractionConversionService.conversion(fractionConversion);
			
			ResponseCookieUtil.addCookie("c", memberDetail.getChinese(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("e", memberDetail.getEnglish(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("m", memberDetail.getMathematics(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("ma", memberDetail.getMathematics_a(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("mb", memberDetail.getMathematics_b(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("s", memberDetail.getSociety(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("n", memberDetail.getNaturally(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("eh", memberDetail.getEnglish_hearing(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("ac", memberDetail.getApcs_concept(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("ap", memberDetail.getApcs_practice(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("c_c", cc!=null ? cc.getOne_years_ago_score() : memberDetail.getChinese(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("e_c", ec!=null ? ec.getOne_years_ago_score() : memberDetail.getEnglish(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("m_c", mc!=null ? mc.getOne_years_ago_score() : memberDetail.getMathematics(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("ma_c", mac!=null ? mac.getOne_years_ago_score() : memberDetail.getMathematics_a(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("mb_c", mbc!=null ? mbc.getOne_years_ago_score() : memberDetail.getMathematics_b(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("s_c", sc!=null ? sc.getOne_years_ago_score() : memberDetail.getSociety(), false, true, "/forecast", -1, "None", pResponse);
			ResponseCookieUtil.addCookie("n_c", nc!=null ? nc.getOne_years_ago_score() : memberDetail.getNaturally(), false, true, "/forecast", -1, "None", pResponse);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pResponse.setCharacterEncoding("utf-8");

		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/deleteExamNum" , method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteExamNum(@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, @ModelAttribute MemberDetail memberDetail, HttpSession session, Model model, HttpServletRequest pRequest) {
		
		try {

			memberDetailService.delete(memberDetail);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("PATH", "/forecast/member/examNumData");
		return "front/toPath";
	}
	
	@RequestMapping(value = "/checkExamNum" , method = {RequestMethod.GET, RequestMethod.POST})
	public void checkExamNum(@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {

		String status = "N";
		String member_id = memberAccountSession.getId();
		String exam_num = pRequest.getParameter("exam_num") == null ? "" : pRequest.getParameter("exam_num");
		String chinese = pRequest.getParameter("chinese") == null ? "" : pRequest.getParameter("chinese");
		String english = pRequest.getParameter("english") == null ? "" : pRequest.getParameter("english");
		String ma = pRequest.getParameter("ma") == null ? "" : pRequest.getParameter("ma");
		String mb = pRequest.getParameter("mb") == null ? "" : pRequest.getParameter("mb");
		String society = pRequest.getParameter("society") == null ? "" : pRequest.getParameter("society");
		String natural = pRequest.getParameter("natural") == null ? "" : pRequest.getParameter("natural");
		String hearing = pRequest.getParameter("hearing") == null ? "" : pRequest.getParameter("hearing");
		String apcs_concept = pRequest.getParameter("apcs_concept") == null ? "" : pRequest.getParameter("apcs_concept");
		String apcs_practice = pRequest.getParameter("apcs_practice") == null ? "" : pRequest.getParameter("apcs_practice");
		
		MemberDetail temp = new MemberDetail();
		MemberDetail memberDetail = new MemberDetail();
		MemberDetail memberDetailByMember = new MemberDetail();//唯一
		
		int year = yearUsePeriodService.getYear();
		
		temp.setYear(String.valueOf(year));
		temp.setExam_number(exam_num);
		temp.setMember_id(member_id);
		memberDetail = memberDetailService.getStatusIsT(temp);
		memberDetailByMember = memberDetailService.dataByMemberAndNum(temp);
		
		if(memberDetail == null) {
			//沒有驗證成功的資料,另外取得非驗證失敗的其他筆資料做比對
			memberDetail = new MemberDetail();
			memberDetail.setYear(String.valueOf(year));
			memberDetail = memberDetailService.dataForCehck(temp);
			if(memberDetail != null) {
				
				if(!chinese.equals(memberDetail.getChinese())) {
					status = "B";
				}
				
				if(!english.equals(memberDetail.getEnglish())) {
					status = "B";
				}
				
				if(!ma.equals(memberDetail.getMathematics_a())) {
					status = "B";
				}
				
				if(!mb.equals(memberDetail.getMathematics_b())) {
					status = "B";
				}

				if(!society.equals(memberDetail.getSociety())) {
					status = "B";
				}
				
				if(!natural.equals(memberDetail.getNaturally())) {
					status = "B";
				}

				if(!hearing.equals(memberDetail.getEnglish_hearing())) {
					status = "B";
				}

				if(!apcs_concept.equals(memberDetail.getApcs_concept())) {
					status = "B";
				}

				if(!apcs_practice.equals(memberDetail.getApcs_practice())) {
					status = "B";
				}
				
			} else {
				
			}
		}else {
			//有驗證成功的資料,無須第二次人工驗證,直接給狀態驗證失敗或成功
			status = "T";
			if(!chinese.equals(memberDetail.getChinese())) {
				status = "F";
			}
			
			if(!english.equals(memberDetail.getEnglish())) {
				status = "F";
			}
			
			if(!ma.equals(memberDetail.getMathematics_a())) {
				status = "F";
			}
			
			if(!mb.equals(memberDetail.getMathematics_b())) {
				status = "F";
			}

			if(!society.equals(memberDetail.getSociety())) {
				status = "F";
			}
			
			if(!natural.equals(memberDetail.getNaturally())) {
				status = "F";
			}

			if(!hearing.equals(memberDetail.getEnglish_hearing())) {
				status = "F";
			}

			if(!apcs_concept.equals(memberDetail.getApcs_concept())) {
				status = "F";
			}

			if(!apcs_practice.equals(memberDetail.getApcs_practice())) {
				status = "F";
			}
		}
		

		
//		System.out.println("mid: "+memberDetailByMember.getMember_id());
//		System.out.println("mid: "+member_id);
		if(memberDetailByMember != null) {
			if(member_id.equals(memberDetailByMember.getMember_id())) {
				status = "C";//重複建立資料
			}
		}
		
//		JSONArray tJSONArray = new JSONArray(departmentList);
		pResponse.setCharacterEncoding("utf-8");

		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/updatePassword" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePassword(@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, @ModelAttribute MemberAccount memberAccount, HttpSession session, Model model) {
		
		//顯示12志願數量
		int wishCount = 0;
		if(memberAccountSession != null) {
			int year = yearUsePeriodService.getYear();
			MemberWish memberWish = new MemberWish();
			memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
			memberWish.setWish_type("12");
			memberWish.setYear(String.valueOf(year));
			wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
		}
		model.addAttribute("WISH_COUNT", wishCount);
		
		//廣告
		Banner banner = new Banner();
		banner.setBanner_type("B");
		List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
		model.addAttribute("bottomList", bottomList);
		
		return "front/member/updatePassword";
	}

	@RequestMapping(value = "/updatePasswordSubmit" , method = RequestMethod.POST)
	public String updatePasswordSubmit(@ModelAttribute MemberAccount memberAccount, HttpSession session, Model model, HttpServletRequest pRequest) {
		MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
		Map map = new HashMap();
		memberAccount.setUpdate_by(memberAccountSession.getEmail());
		memberAccount.setEmail(memberAccountSession.getEmail());

		try{

			map = memberAPIService.updatePassword(memberAccount);
			model.addAttribute("MESSAGE", " 修改密碼成功");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("PATH", "/forecast/member/update");
		return "front/toPath";
	}
	
	@RequestMapping(value = "/checkPassword" , method = {RequestMethod.GET, RequestMethod.POST})
	public void checkPassword(@SessionAttribute("memberAccountSession") MemberAccount memberAccountSession, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {

		String status = "T";
		String password = pRequest.getParameter("oldPW") == null ? "" : pRequest.getParameter("oldPW");
		
		MemberAccount memberAccount = new MemberAccount();
		memberAccount.setEmail(memberAccountSession.getEmail());
		memberAccount.setPassword(password);
		
		Map login = memberAPIService.login(memberAccount);
		
		if(login != null && "1".equals(login.get("STATUS").toString())) {
//			System.out.println("T");
		}else {
			status = "F";
//			System.out.println("F");
		}
		
//		JSONArray tJSONArray = new JSONArray(departmentList);
		pResponse.setCharacterEncoding("utf-8");

		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/forgetPD" , method = {RequestMethod.GET, RequestMethod.POST})
	public String forgetPD(@ModelAttribute MemberAccount memberAccount, HttpSession session, Model model) {

		try {
			//顯示12志願數量
			int wishCount = 0;
			MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
			if(memberAccountSession != null) {
				int year = yearUsePeriodService.getYear();
				MemberWish memberWish = new MemberWish();
				memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
				memberWish.setWish_type("12");
				memberWish.setYear(String.valueOf(year));
				wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
			}
			model.addAttribute("WISH_COUNT", wishCount);
			
			//廣告
			Banner banner = new Banner();
			banner.setBanner_type("B");
			List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
			model.addAttribute("bottomList", bottomList);
			
//			List<Map> schoolDegreeList = tkbAPIService.getSchoolDegreeList();
//			List<Map> schoolAreaList = tkbAPIService.getSchoolAreaList();
//			List<Map> sourceList = tkbAPIService.getSourceList();
//			
//			model.addAttribute("schoolDegreeList", schoolDegreeList);
//			model.addAttribute("schoolAreaList", schoolAreaList);
//			model.addAttribute("sourceList", sourceList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "front/member/forgetPD";
	}
	
	@RequestMapping(value = "/setPD" , method = {RequestMethod.GET, RequestMethod.POST})
	public void setPD(HttpServletRequest pRequest, HttpServletResponse pResponse, HttpSession session, Model model) {
		
		String email = pRequest.getParameter("email") != null ? pRequest.getParameter("email") : "";
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		list.add(map);
		
		MemberAccount memberAccount = new MemberAccount();
		memberAccount.setEmail(email);
		memberAccount = memberAccountService.data(memberAccount);
		
		if(memberAccount != null) {
			
			String password = generator(10);
//			System.out.println("password:"+password);
			
			memberAccount.setUpdate_by(memberAccount.getEmail());
			memberAccount.setPassword(password);
			try {
				Map temp = memberAPIService.updatePassword(memberAccount);
				String content = "您好,\n您的新密碼為"+password+",\n請登入後立即修改密碼,謝謝。";
				
				commonService.sendGmail(email, htmlTitle+"-忘記密碼", content);

//				String content = "Hi, your new password is : "+password+"\nPlease re-login TKB Forecast System, and modify your password.\n\nBy Taiwan Knowledge Bank";
//				commonService.sendEmail(email, "Welcome to TKB Forecast System - New Password", content);
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			map.put("STATUS", "Y");
			map.put("MSG", "寄送完成");
		} else {
			map.put("STATUS", "N");
			map.put("MSG", "無此帳號");
		}
		
//		map.put("STATUS", "Y");
//		map.put("MSG", "追蹤成功");
		
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
	public String forgetPDSuccess(@ModelAttribute MemberAccount memberAccount, HttpSession session, Model model) {

		try {
			//顯示12志願數量
			int wishCount = 0;
			MemberAccount memberAccountSession = (MemberAccount)session.getAttribute("memberAccountSession");
			if(memberAccountSession != null) {
				int year = yearUsePeriodService.getYear();
				MemberWish memberWish = new MemberWish();
				memberWish.setExam_number_id(memberAccountSession.getExam_number_id());
				memberWish.setWish_type("12");
				memberWish.setYear(String.valueOf(year));
				wishCount = memberWishService.countByExamNumberIdAndSchoolDepartmentId(memberWish);
			}
			model.addAttribute("WISH_COUNT", wishCount);
			
			//廣告
			Banner banner = new Banner();
			banner.setBanner_type("B");
			List<Map<String, Object>> bottomList = bannerService.frontRandList(banner);
			model.addAttribute("bottomList", bottomList);
			
//			List<Map> schoolDegreeList = tkbAPIService.getSchoolDegreeList();
//			List<Map> schoolAreaList = tkbAPIService.getSchoolAreaList();
//			List<Map> sourceList = tkbAPIService.getSourceList();
//			
//			model.addAttribute("schoolDegreeList", schoolDegreeList);
//			model.addAttribute("schoolAreaList", schoolAreaList);
//			model.addAttribute("sourceList", sourceList);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	}// end of generator

	@RequestMapping(value = "/serialnumber/login", method = {RequestMethod.GET})
	public String serialnumberLogin() {

		return "front/member/snLogin";
	}

	@RequestMapping(value = "/serialnumber/login", method = {RequestMethod.POST})
	@ResponseBody
	@CrossOrigin
	public ApiReturnObject<Void> doSerialnumberLogin(
			@RequestParam(value = "email") String email,
			@RequestParam(value = "serial_number") String serial_number,
			@RequestParam(value = "password") String password,
			Model model, 
			HttpServletRequest pRequest) {
		// 判斷序號及序號密碼是否正確
		Authorization authorization = new Authorization();
		authorization.setSerial_number(serial_number);
		authorization.setPassword(password);
		authorization = authorizationService.getFrontData(authorization);
		if(authorization == null) return new ApiReturnObject<Void>(202, "請確認序號&密碼是否有誤");

		// 判斷Email 是否已被註冊
		try{
			Map MemberAccountList = memberAPIService.getCheckMemberAccountEmail(email);

			// 未註冊 - 寄送補註冊連結至電子信箱 - 回傳 請至信箱收信
			if(MemberAccountList!=null && "0".equals(MemberAccountList.get("STATUS").toString())) {
				String code = BaseUtil.encryptStr(email + "/" + serial_number + "/" + password);
				String url = "https://www.reallygood.com.tw/forecast/member/serialnumber/register/" + code;
				String title = "";
				String content = "" + url;
				
				commonService.sendEmail(email, title, content);
				return new ApiReturnObject<Void>(201, "請至信箱收信");
			}
			// 已註冊 - 序號綁定流程 - 回傳 綁定結果
			else {
				MemberAccount data = new MemberAccount();
				data.setEmail(email);
				data = memberAccountService.data(data);
				
				//搜尋輸入序號資料並算出時效
				authorization = authorizationService.getFrontData(authorization);
				
				Calendar date = Calendar.getInstance();
				DateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
				String yyyymmddStr = yyyymmdd.format(date.getTime());
				authorization.setBegin_date(yyyymmddStr);
				
				date.add(Calendar.DATE, 365);
				yyyymmddStr = yyyymmdd.format(date.getTime());
				authorization.setEnd_date(yyyymmddStr);
		
				String ip = pRequest.getRemoteAddr();
				authorization.setIp(ip);
				authorization.setUse_by(email);
				authorization.setStatus("1");
				authorization.setMember_id(data.getId());
				authorizationService.frontUpdate(authorization);
				
				//更新帳號權限
				Identity identity = new Identity();
				identity.setName("前台");
				identity.setLevel("1");
				int identity_id = identityService.getFrontIdentity(identity);
				MemberAccount memberAccount = new MemberAccount();
				memberAccount.setIdentity_id(String.valueOf(identity_id));
				memberAccount.setId(data.getId());
				memberAccountService.updateIdentity(memberAccount);
				return new ApiReturnObject<Void>(200, "已將序號與" + email + "進行綁定");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ApiReturnObject<Void>(400, "");
	}

	@RequestMapping(value = "/serialnumber/register/{code}", method = {RequestMethod.GET})
	public String serialnumberRegister(
			@PathVariable("code") String code,
			@ModelAttribute MemberAccount memberAccount,
			Model model) {
		// 將加密序號取出放入model
		String[] origin = BaseUtil.decryptStr(code).split("/");
		String email = origin[0];
		memberAccount.setEmail(email);
		model.addAttribute("code", code);
		return "front/member/snRegister";
	}

	@RequestMapping(value = "/serialnumber/register/addSubmit", method = {RequestMethod.POST})
	public String doSerialnumberRegister(
			@RequestParam(value = "code") String code,
			@ModelAttribute MemberAccount memberAccount,
			Model model, 
			HttpServletRequest pRequest) {
		model.addAttribute("PATH", "/forecast/index");
		
		// 將加密序號取出並與form 表單提交資料進行比對
		String[] origin = BaseUtil.decryptStr(code).split("/");
		String origin_email = origin[0], serial_number = origin[1], password = origin[2];
		if(!memberAccount.getEmail().equals(origin_email)) {
			model.addAttribute("MESSAGE", "Email 比對錯誤");
			return "front/toPath";
		}

		// 註冊流程
		memberAccount.setCreate_by(memberAccount.getChinese_name());
		memberAccount.setUpdate_by(memberAccount.getChinese_name());
		memberAccount.setFb_uid("0");
		memberAccount.setWebsite("FC");
		try{
			Map MemberAccountList = memberAPIService.getCheckMemberAccountEmail(memberAccount.getEmail());
			
			if(MemberAccountList!=null && "0".equals(MemberAccountList.get("STATUS").toString())) {
				memberAPIService.add(memberAccount);
				
				Identity identity = new Identity();
				identity.setName("前台");
				int identity_id = identityService.getBasicIdentity(identity);
				if(identity_id > 0) {
					int id = utilService.getNextId("forecast", "member_account");
					memberAccount.setId(String.valueOf(id));
					memberAccount.setStatus("1");
					memberAccount.setIdentity_id(String.valueOf(identity_id));
					memberAccount.setName(memberAccount.getChinese_name());
					
					String cKey = "tkbs_tkbt_foreca";
					Calendar date = Calendar.getInstance();
					DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMddHHmmss");
					String yyyymmddStr = yyyymmdd.format(date.getTime());
					String temp = memberAccount.getEmail() + "," + yyyymmddStr;
					String content = encrypt(temp, cKey);
					memberAccount.setVerification_status("0");
					memberAccount.setVerification_code(URLEncoder.encode( content, "UTF-8" ));
					memberAccountService.add(memberAccount);
					
					sendEmail(memberAccount.getEmail(), URLEncoder.encode( content, "UTF-8" ));
				}
			} else {
				model.addAttribute("MESSAGE", "請勿重複註冊");
				return "front/toPath";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 判斷序號及序號密碼是否正確
		Authorization authorization = new Authorization();
		authorization.setSerial_number(serial_number);
		authorization.setPassword(password);
		authorization = authorizationService.getFrontData(authorization);
		if(authorization == null) {
			model.addAttribute("MESSAGE", "請確認序號&密碼是否有誤");
			return "front/toPath";
		}

		// 綁定序號		
		MemberAccount data = new MemberAccount();
		data.setEmail(memberAccount.getEmail());
		data = memberAccountService.data(data);
		
		//搜尋輸入序號資料並算出時效
		authorization = authorizationService.getFrontData(authorization);
		
		Calendar date = Calendar.getInstance();
		DateFormat yyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
		String yyyymmddStr = yyyymmdd.format(date.getTime());
		
		authorization.setBegin_date(yyyymmddStr);
		
		date.add(Calendar.DATE, 365);
		yyyymmddStr = yyyymmdd.format(date.getTime());
		authorization.setEnd_date(yyyymmddStr);

		String ip = pRequest.getRemoteAddr();
		authorization.setIp(ip);
		authorization.setUse_by(memberAccount.getEmail());
		authorization.setStatus("1");
		authorization.setMember_id(data.getId());
		authorizationService.frontUpdate(authorization);
		
		//更新帳號權限
		Identity identity = new Identity();
		identity.setName("前台");
		identity.setLevel("1");
		int identity_id = identityService.getFrontIdentity(identity);
		memberAccount.setIdentity_id(String.valueOf(identity_id));
		memberAccount.setId(data.getId());
		memberAccountService.updateIdentity(memberAccount);
		model.addAttribute("MESSAGE", "已將序號與" + memberAccount.getEmail() + "進行綁定");
		return "front/toPath";
		
	}

}

