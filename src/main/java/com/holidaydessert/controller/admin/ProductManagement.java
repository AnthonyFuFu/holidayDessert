package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Product;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.ProductCollectionService;
import com.holidaydessert.service.ProductPicService;
import com.holidaydessert.service.ProductService;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/admin/product")
@SessionAttributes("employeeSession")
@CrossOrigin
@ApiIgnore
public class ProductManagement {

	@Value("${admin.upload.file.path}")
	private String ADMIN_UPLOAD_FILE_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCollectionService productCollectionService;
	
	@Autowired
	private ProductPicService productPicService;

	@Autowired
	private CommonService commonService;

	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		List<Map<String, Object>> productList = productService.getPicList();
		for (int i = 0; i < productList.size(); i++) {
			ProductPic productPic = new ProductPic();
			productPic.setPdId(productList.get(i).get("PD_ID").toString());
			List<Map<String, Object>> productPicList = productPicService.list(productPic);
			productList.get(i).put("productPicList", productPicList);
		}
		
		model.addAttribute("authorityList", authorityList);
		model.addAttribute("productList", productList);
		return "admin/product/list";

	}
	
	@GetMapping("/productTables")
	public void productTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Product productData = new Product();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		productData.setStart(start);
		productData.setLength(length);
		productData.setSearchText(searchValue);
		
		List<Map<String, Object>> productList = productService.list(productData);

		if (productList == null) {
			productList = new ArrayList<Map<String, Object>>();
		}

		int count = productService.getCount(productData);

		product.setRecordsFiltered(count);
		product.setRecordsTotal(count);
		product.setData(productList);
		product.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(product);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@GetMapping("/productCollectionTables")
	public void productCollectionTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductCollection productCollection, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		ProductCollection productCollectionData = new ProductCollection();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		productCollectionData.setStart(start);
		productCollectionData.setLength(length);
		productCollectionData.setSearchText(searchValue);
		
		List<Map<String, Object>> productCollectionList = productCollectionService.list(productCollectionData);

		if (productCollectionList == null) {
			productCollectionList = new ArrayList<Map<String, Object>>();
		}

		int count = productCollectionService.getCount(productCollectionData);

		productCollection.setRecordsFiltered(count);
		productCollection.setRecordsTotal(count);
		productCollection.setData(productCollectionList);
		productCollection.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(productCollection);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/addProduct" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addProduct(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> productCollectionList = productCollectionService.getList();
		
		try {
			Product product = new Product();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productCollectionList", productCollectionList);
			model.addAttribute("product", product);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/product/productForm";
	}
	
	@RequestMapping(value = "/updateProduct" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateProduct(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> productCollectionList = productCollectionService.getList();
		
		try {
			product = productService.getData(product);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productCollectionList", productCollectionList);
			model.addAttribute("product", product);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/product/productForm";
	}
	
	@RequestMapping(value = "/productAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			product.setPdCreateBy(employeeSession.getEmpName());
			product.setPdUpdateBy(employeeSession.getEmpName());
			productService.add(product);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/product/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/productUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			product.setPdUpdateBy(employeeSession.getEmpName());
			productService.update(product);
			model.addAttribute("PATH", "/holidayDessert/admin/product/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/addProductCollection" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addProductCollection(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			ProductCollection productCollection = new ProductCollection();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productCollection", productCollection);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/product/productCollectionForm";
	}
	
	@RequestMapping(value = "/updateProductCollection" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateProductCollection(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductCollection productCollection, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			productCollection = productCollectionService.getData(productCollection);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productCollection", productCollection);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/product/productCollectionForm";
	}
	
	@RequestMapping(value = "/productCollectionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productCollectionAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductCollection productCollection,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			productCollectionService.add(productCollection);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/product/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/productCollectionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productCollectionUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductCollection productCollection,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			productCollectionService.update(productCollection);
			model.addAttribute("PATH", "/holidayDessert/admin/product/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/editProductPic" , method = {RequestMethod.GET, RequestMethod.POST})
	public String editProductPic(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product,@ModelAttribute ProductPic productPic, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		productPic.setPdId(product.getPdId());
		List<Map<String, Object>> productPicList = productPicService.list(productPic);
		
		try {
			product = productService.getData(product);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("product", product);
			model.addAttribute("productPicList", productPicList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/product/editProductPic";
	}
	
	@RequestMapping(value = "/addProductPic" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addProductPic(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Product product, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		product = productService.getData(product);
		
		List<Map<String, Object>> productList = productService.getList();
		
		try {
			ProductPic productPic = new ProductPic();
			productPic.setPdId(product.getPdId());
			productPic.setPdName(product.getPdName());
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productList", productList);
			model.addAttribute("productPic", productPic);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/product/productPicForm";
	}
	
	@RequestMapping(value = "/updateProductPic" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateProductPic(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductPic productPic, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			productPic = productPicService.getData(productPic);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("productPic", productPic);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/product/productPicForm";
	}
	

	@RequestMapping(value = "/productPicAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productPicAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductPic productPic,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			String osName = System.getProperty("os.name").toLowerCase();

			if (osName.contains("win")) {
				productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images\\productPic\\"));
			} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
				productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/productPic/"));
			} else if (osName.contains("mac")) {
				productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/productPic/"));
			} else {
				productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/productPic/"));
			}
			productPic.setPdPicture("holidayDessert/admin/upload/images/productPic/" + productPic.getPdImage());
			
			productPicService.add(productPic);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/product/editProductPic?pdId="+productPic.getPdId());

		return "admin/toPath";
	}

	@RequestMapping(value = "/productPicUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productPicUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductPic productPic,
			@RequestParam(value = "imageFile") MultipartFile imageFile,
			@RequestParam(value = "originalImage", required = false) String originalImage,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				productPic.setPdImage(originalImage);
			} else {
				String osName = System.getProperty("os.name").toLowerCase();
				if (osName.contains("win")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images\\productPic\\");
					productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images\\productPic\\"));
				} else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/productPic/");
					productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/productPic/"));
				} else if (osName.contains("mac")) {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/productPic/");
					productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/productPic/"));
				} else {
					commonService.deleteUploadedFiles(originalImage, ADMIN_UPLOAD_FILE_PATH + "images/productPic/");
					productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile,ADMIN_UPLOAD_FILE_PATH + "images/productPic/"));
				}
			}
			productPic.setPdPicture("holidayDessert/admin/upload/images/productPic/" + productPic.getPdImage());
			
			productPicService.update(productPic);
			model.addAttribute("PATH", "/holidayDessert/admin/product/editProductPic?pdId="+productPic.getPdId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/productPicDelete" , method = {RequestMethod.POST})
	@ResponseBody
	public String productPicDelete(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute ProductPic productPic, Model model, HttpServletRequest request) {
		
		productPic = productPicService.getData(productPic);
		productPicService.delete(productPic);
		
		return "editProductPic?pdId="+productPic.getPdId();
	}
	
}
