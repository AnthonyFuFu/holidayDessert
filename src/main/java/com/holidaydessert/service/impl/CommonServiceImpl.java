package com.holidaydessert.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.holidaydessert.service.CommonService;
import com.holidaydessert.utils.BaseUtil;

@Service
public class CommonServiceImpl implements CommonService {
	
	@Value("${mail.fromname}")
	private String fromname;
	
    private Random ran = new Random();
     	/**
     	 *  多個檔案上傳的接口
     	 **/
//    @PostMapping("/api/upload/multi")
//    public ResponseEntity<?> uploadFileMulti(
//            @RequestParam("extraField") String extraField,
//            @RequestParam("files") MultipartFile[] uploadfiles) {
//
//
//    			  // 取得檔案名稱
//        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
//                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
//
//        if (StringUtils.isEmpty(uploadedFileName)) {
//            return new ResponseEntity("請選擇檔案!", HttpStatus.OK);
//        }
//
//        try {
//
//            saveUploadedFiles(Arrays.asList(uploadfiles));
//
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity("Successfully uploaded - "
//                + uploadedFileName, HttpStatus.OK);
//
//    }
    	/**
    	 *  多個檔案上傳的接口
    	 **/
//    @PostMapping("/api/upload/multi/model")
//    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadFileModel model) {
//
//        try {
//
//            saveUploadedFiles(Arrays.asList(model.getFiles()));
//
//        } catch (IOException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity("Successfully uploaded!", HttpStatus.OK);
//
//    }
 //將檔案儲存
   public void saveUploadedFiles(List<MultipartFile> files,String UPLOADED_FOLDER) throws IOException {
	   for (MultipartFile file : files) {
		   if (file.isEmpty()) {
			   continue; //next pls
		   		}
		   byte[] bytes = file.getBytes();
		   Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
		   Files.write(path, bytes);
        }
    }
    
   public void saveUploadedFiles(MultipartFile files,String UPLOADED_FOLDER) throws IOException {
	   byte[] bytes = files.getBytes();
	   Path path = Paths.get(UPLOADED_FOLDER + files.getOriginalFilename());
	   Files.write(path, bytes);
       }
   
   
	/**
	 * 上傳機制，回傳以年日月時分秒ID組成的檔名
	 * @param filePath
	 * @param fileStream
	 * @param fileName
	 * @param id
	 * @param extension
	 * @return String fileName
	 * @throws IOException
	 */
   public String saveByDateNameUploadedFiles(MultipartFile files,String UploadedFolder) throws IOException {
	   File file = new File(files.getOriginalFilename());
	   byte[] bytes = files.getBytes();
	
	   String fileName = "";
	   
	   		//若上傳檔案為空則回傳原本檔名
	 		if (files == null || "".equals(files)) {
	 			return files.getOriginalFilename();
	 		}
	 		//以"年月日時分秒ID副檔名"來重新命名檔案
	 		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date())
	 				 + "." + files.getOriginalFilename().substring(files.getOriginalFilename().length()-3);
	 		
	 		try {			
	 			Path path = Paths.get(UploadedFolder+fileName);
	 
	 		  Files.write(path, bytes);
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		}
	 		return fileName;
       }
   
   public void deleteUploadedFiles(String fileName,String UPLOADED_FOLDER) throws IOException {
	   	try{
	   		Path path = Paths.get(UPLOADED_FOLDER + fileName);
		   	if(Files.exists(path)){
		   		Files.delete(path);
		   		}
		   	
	   	}catch(Exception ex){
	   			ex.printStackTrace();
	   		}
      }
   
   public boolean checkImageWidth(MultipartFile filePath,int maxWidth) throws IOException {
//		File imgfile = new File(filePath);
		BufferedImage buff = ImageIO.read(filePath.getInputStream());
		
		if(buff.getWidth() == maxWidth){
			return true;
		}else{
			return false;
		}
		
	}
   
   public boolean checkImageHeight(MultipartFile filePath,int maxHeight) throws IOException {
		
//		File imgfile = new File(filePath);
		BufferedImage buff = ImageIO.read(filePath.getInputStream());
		
		if(buff.getHeight() == maxHeight){
			return true;
		}else{
			return false;
		}
		
	}
   
	//寄送信件
	public void sendEmail(String email, String title, String content) throws IOException {
		try {
        	Properties properties = System.getProperties();// 獲取系統屬性
		 	
		 	properties.setProperty("mail.transport.protocol", "smtp");
	        properties.setProperty("mail.smtp.host", "mail.tkbnews.com.tw");// 設置郵件服務器
	        properties.setProperty("mail.smtp.auth", "true");// 打開認證
//	        properties.setProperty("mail.user", "ifUserNameNeeded");
//	        properties.setProperty("mail.password", "ifPasswordNeeded");
	        
	        Session mailSession = Session.getDefaultInstance(properties, null);
//            Transport transport = mailSession.getTransport();
            InternetAddress from = new InternetAddress("service@tkbnews.com.tw");
            // 產生整封 email 的主體 message
            MimeMessage msg = new MimeMessage(mailSession);
            
            msg.setSubject(title);
            msg.setFrom(from);
    		msg.setRecipients(Message.RecipientType.TO, email);
    		msg.setText(content);
//            //QQ郵箱需要下面這段代碼，163郵箱不需要
//            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//            sf.setTrustAllHosts(true);
//            properties.put("mail.smtp.ssl.enable", "true");
//            properties.put("mail.smtp.ssl.socketFactory", sf);
    		
        	Transport transport = mailSession.getTransport("smtp");
        	transport.connect("mail.tkbnews.com.tw", "byone@tkbnews.com.tw", "j;6m06vu06");
        	transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
    		transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	//寄送信件
	public void sendGmail(String email, String title, String content) throws IOException {
		try {
			
			String user = "marking00@tkb.com.tw";
			String pass = "liuucflblyhchqux";
			String form = "marking00@tkb.com.tw";
			
        	Properties properties = System.getProperties();// 獲取系統屬性
		 	
        	properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "smtp.gmail.com");// 設置郵件服務器
	        properties.put("mail.smtp.auth", "true");// 打開認證
	        properties.put("mail.smtp.port", "587");
	        properties.put("mail.smtp.user", user);
	        properties.put("mail.smtp.password", pass);
	        
	        Session mailSession = Session.getDefaultInstance(properties);
//          Transport transport = mailSession.getTransport();
	        
	        InternetAddress from = new InternetAddress(form, MimeUtility.encodeText(fromname,"UTF-8","b"));
	        // 產生整封 email 的主體 message
	        MimeMessage msg = new MimeMessage(mailSession);
	        msg.setHeader("Content-Type", "text/html; charset=UTF-8");
	        msg.setSubject(title, "UTF-8");
	        msg.setFrom(from);
	        msg.setRecipients(Message.RecipientType.TO, email);
	        msg.setContent(content, "text/html; charset=UTF-8");
    		
	        Transport transport = mailSession.getTransport("smtp");
        	transport.connect("smtp.gmail.com", user, pass);
        	transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
    		transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	//取得序號
	public String[] makeNumber(String email) {
		
		String[] str = new String[2];
		String serial = "", password = "";
		
		try {
			
			Calendar date = Calendar.getInstance();
			DateFormat nowDate = new SimpleDateFormat("yyyyMMddHHmmss");
			String timeStr = nowDate.format(date.getTime());
			String cKey = "tkbst_foreca_"+String.valueOf(Integer.valueOf(timeStr.substring(0, 4))-1911);
			
//			Random ran = new Random();
			int ranNum = ran.nextInt(99998)+1;
			String ranText = String.format("%05d", ranNum);
			String cSrc = timeStr+ranText;
	
			String enString = BaseUtil.encrypt(cSrc, cKey);
	
			String[] tokens = enString.split("");
	
			String regexpStr = "^[0-9A-Za-z]+$";
			
			int temp = 0;
			for(int i = 0;i < tokens.length;i++) {
				if(tokens[i].matches(regexpStr) ){
					temp++;
					serial += tokens[i];
				}else {
					continue;
				}
				if(temp==16) {
					break;
				}
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return str;
		
	}
	
	//設定執行陣列
	public String[][] execution(int[] filter) {
		int[] filter_temp = Arrays.copyOf(filter, filter.length);
		String[] subject = {"c","e","m","ma","mb","s","n","t"};
		for(int i=0; i<filter_temp.length-1; i++) {
			for (int j=0;j<filter_temp.length-i-1;j++) {
				if (filter_temp[j+1]>filter_temp[j]) {
					int temp = filter_temp[j+1];  //交換陣列元素
					filter_temp[j+1]=filter_temp[j];
					filter_temp[j]= temp;
					
					String str = subject[j+1];
					subject[j+1]=subject[j];
					subject[j]=str;
				}
			}
		}
		
		String[][] execution = new String[8][8];
		int temp = 0;
		int layer1=0, layer2=0;
		for(int i=0; i<filter_temp.length; i++) {
			if(filter_temp[i] > 0) {
				if(temp == 0 || filter_temp[i] == temp) {
					execution[layer1][layer2] = subject[i];
				} else {
					layer1++;
					layer2 = 0;
					execution[layer1][layer2] = subject[i];
				}
				layer2++;
				temp = filter_temp[i];
			} else {
				break;
			}
		}
		
		return execution;
		
	}
   
}
