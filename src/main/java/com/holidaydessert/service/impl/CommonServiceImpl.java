package com.holidaydessert.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

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

@Service
public class CommonServiceImpl implements CommonService {

	private final static String HOST = "smtp.gmail.com";
	private final static String AUTH = "true";
	private final static String PORT = "587";
	private final static String STARTTLE_ENABLE = "true";
//	private final static String SENDER_NAME = "假日甜點";
	private final static String SENDER = "s9017611@gmail.com";
	private final static String PASSWORD = "rkun bgju hpcf egfm";
	
	@Value("${mail.fromname}")
	private String fromname;

	/**
	 * 多個檔案上傳的接口
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
	 * 多個檔案上傳的接口
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
	
	// 將檔案儲存
	public void saveUploadedFiles(List<MultipartFile> files, String UPLOADED_FOLDER) throws IOException {
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				continue; // next pls
			}
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
		}
	}
	
	public void saveUploadedFiles(MultipartFile files, String UPLOADED_FOLDER) throws IOException {
		byte[] bytes = files.getBytes();
		Path path = Paths.get(UPLOADED_FOLDER + files.getOriginalFilename());
		Files.write(path, bytes);
	}
	
	public String saveByDateNameUploadedFiles(MultipartFile files, String UploadedFolder) throws IOException {

		byte[] bytes = files.getBytes();
		String fileName = "";
		
		File folder = new File(UploadedFolder);
		if (!folder.exists()) {
		    // 如果資料夾不存在，則建立該資料夾
		    folder.mkdirs();
		}
		
		// 若上傳檔案為空則回傳原本檔名
		if (files == null || files.isEmpty()) {
			return files.getOriginalFilename();
		}
		// 以"年月日時分秒ID副檔名"來重新命名檔案
		fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "."
				+ files.getOriginalFilename().substring(files.getOriginalFilename().length() - 3);

		try {
			Path path = Paths.get(UploadedFolder + fileName);
			Files.write(path, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;

	}

	public void deleteUploadedFiles(String fileName, String UPLOADED_FOLDER) throws IOException {
		try {
			Path path = Paths.get(UPLOADED_FOLDER + fileName);
			if (Files.exists(path)) {
				Files.delete(path);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public boolean checkImageWidth(MultipartFile filePath, int maxWidth) throws IOException {

		BufferedImage buff = ImageIO.read(filePath.getInputStream());

		if (buff.getWidth() == maxWidth) {
			return true;
		} else {
			return false;
		}

	}

	public boolean checkImageHeight(MultipartFile filePath, int maxHeight) throws IOException {

		BufferedImage buff = ImageIO.read(filePath.getInputStream());

		if (buff.getHeight() == maxHeight) {
			return true;
		} else {
			return false;
		}

	}

	// 寄送信件
	public void sendEmail(String email, String title, String content) throws IOException {
		try {
			Properties properties = System.getProperties();// 獲取系統屬性

			properties.setProperty("mail.transport.protocol", "smtp");
			properties.setProperty("mail.smtp.host", HOST);// 設置郵件服務器
			properties.setProperty("mail.smtp.auth", AUTH);// 打開認證

			Session mailSession = Session.getDefaultInstance(properties, null);
			InternetAddress from = new InternetAddress("s9017611@gmail.com");
			// 產生整封 email 的主體 message
			MimeMessage msg = new MimeMessage(mailSession);

			msg.setSubject(title);
			msg.setFrom(from);
			msg.setRecipients(Message.RecipientType.TO, email);
			msg.setText(content);

			Transport transport = mailSession.getTransport("smtp");
			transport.connect(HOST, SENDER, PASSWORD);
			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 寄送信件
	public void sendGmail(String email, String title, String content) throws IOException {
		try {
			
			Properties properties = System.getProperties();// 獲取系統屬性

			properties.put("mail.smtp.starttls.enable", STARTTLE_ENABLE);
			properties.put("mail.smtp.host", HOST);// 設置郵件服務器
			properties.put("mail.smtp.auth", AUTH);// 打開認證
			properties.put("mail.smtp.port", PORT);
			properties.put("mail.smtp.user", SENDER);
			properties.put("mail.smtp.password", PASSWORD);

			Session mailSession = Session.getDefaultInstance(properties);

			InternetAddress from = new InternetAddress(SENDER, MimeUtility.encodeText(fromname, "UTF-8", "b"));
			// 產生整封 email 的主體 message
			MimeMessage msg = new MimeMessage(mailSession);
			msg.setHeader("Content-Type", "text/html; charset=UTF-8");
			msg.setSubject(title, "UTF-8");
			msg.setFrom(from);
			msg.setRecipients(Message.RecipientType.TO, email);
			msg.setContent(content, "text/html; charset=UTF-8");

			Transport transport = mailSession.getTransport("smtp");
			transport.connect(HOST, SENDER, PASSWORD);
			transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
