package com.holidaydessert.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
public interface CommonService {
	/**
 	 *  多個檔案上傳的接口
 	 **/
//	@PostMapping("/api/upload/multi")
//	public ResponseEntity<?> uploadFileMulti(
//			@RequestParam("extraField") String extraField,
//			@RequestParam("files") MultipartFile[] uploadfiles);
	/**
	 *  多個檔案上傳的接口
	 **/
//	@PostMapping("/api/upload/multi/model")
//	public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadFileModel model);
	
	//多檔案上傳
  public void saveUploadedFiles(List<MultipartFile> files,String UPLOADED_FOLDER) throws IOException;
  	//單一檔案上傳存取實體上傳名稱方法
  public void saveUploadedFiles(MultipartFile files,String UPLOADED_FOLDER) throws IOException;
  
  	//將檔案儲存,檔名用日期後的方式存檔
  public String saveByDateNameUploadedFiles(MultipartFile files,String UPLOADED_FOLDER) throws IOException;
  
  public void deleteUploadedFiles(String fileName,String UPLOADED_FOLDER) throws IOException;
  
  public boolean checkImageWidth(MultipartFile filePath,int maxWidth) throws IOException;
  
  public boolean checkImageHeight(MultipartFile filePath,int maxHeight) throws IOException;
  
  public void sendEmail(String email, String title, String content) throws IOException;
  
  public void sendGmail(String email, String title, String content) throws IOException;
  
  public String[] makeNumber(String email);
  
  public String[][] execution(int[] filter);
}
