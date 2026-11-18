package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Product;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.ProductCollectionService;
import com.holidaydessert.service.ProductPicService;
import com.holidaydessert.service.ProductService;
import static com.holidaydessert.constant.BuildPath.*;
import static com.holidaydessert.utils.ControllerUtil.*;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin/product")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class ProductManagement {
	
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
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		List<Map<String, Object>> productList = productService.getPicList();
		for (int i = 0; i < productList.size(); i++) {
			ProductPic productPic = new ProductPic();
			productPic.setPdId(productList.get(i).get("PD_ID").toString());
			List<Map<String, Object>> productPicList = productPicService.list(productPic);
			productList.get(i).put("productPicList", productPicList);
		}
		
		model.addAttribute("productList", productList);
		return "admin/product/list";

	}
	
	@GetMapping("/productTables")
	public void productTables(@SessionAttribute Employee employeeSession,
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
	public void productCollectionTables(@SessionAttribute Employee employeeSession,
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
	public String addProduct(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("productCollectionList", safe("productCollectionList", productCollectionService::getList, Collections.emptyList()));
		model.addAttribute("product", new Product());
		return "admin/product/productForm";
	}
	
	@RequestMapping(value = "/updateProduct" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateProduct(@SessionAttribute Employee employeeSession,
			@ModelAttribute Product product, Model model) {

		model.addAttribute("productCollectionList", safe("productCollectionList", productCollectionService::getList, Collections.emptyList()));
		model.addAttribute("product", productService.getData(product));
		return "admin/product/productForm";
	}
	
	@RequestMapping(value = "/productAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Product product, Model model) {

		product.setPdCreateBy(employeeSession.getEmpName());
		product.setPdUpdateBy(employeeSession.getEmpName());
		return addSubmit(model, ADMIN_PRODUCT_LIST, () -> productService.add(product));
	}
	
	@RequestMapping(value = "/productUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Product product, Model model) {

		product.setPdUpdateBy(employeeSession.getEmpName());
		return updateSubmit(model, ADMIN_PRODUCT_LIST, () -> productService.update(product));
	}
	
	@RequestMapping(value = "/addProductCollection" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addProductCollection(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("productCollection", new ProductCollection());
		return "admin/product/productCollectionForm";
	}
	
	@RequestMapping(value = "/updateProductCollection" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateProductCollection(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductCollection productCollection, Model model) {

		model.addAttribute("productCollection", productCollectionService.getData(productCollection));
		return "admin/product/productCollectionForm";
	}
	
	@RequestMapping(value = "/productCollectionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productCollectionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductCollection productCollection, Model model) {

		return addSubmit(model, ADMIN_PRODUCT_LIST, () -> productCollectionService.add(productCollection));
	}
	
	@RequestMapping(value = "/productCollectionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productCollectionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductCollection productCollection, Model model) {

		return updateSubmit(model, ADMIN_PRODUCT_LIST, () -> productCollectionService.update(productCollection));
	}
	
	@RequestMapping(value = "/editProductPic" , method = {RequestMethod.GET, RequestMethod.POST})
	public String editProductPic(@SessionAttribute Employee employeeSession,
			@ModelAttribute Product product, @ModelAttribute ProductPic productPic, Model model) {

		productPic.setPdId(product.getPdId());
		model.addAttribute("product", productService.getData(product));
		model.addAttribute("productPicList", productPicService.list(productPic));
		return "admin/product/editProductPic";
	}
	
	@RequestMapping(value = "/addProductPic" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addProductPic(@SessionAttribute Employee employeeSession,
			@ModelAttribute Product product, Model model) {

		Product productData = productService.getData(product);
		ProductPic productPic = new ProductPic();
		productPic.setPdId(productData.getPdId());
		productPic.setPdName(productData.getPdName());
		model.addAttribute("productList", safe("productList", productService::getList, Collections.emptyList()));
		model.addAttribute("productPic", productPic);
		return "admin/product/productPicForm";
	}
	
	@RequestMapping(value = "/updateProductPic" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateProductPic(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductPic productPic, Model model) {

		model.addAttribute("productPic", productPicService.getData(productPic));
		return "admin/product/productPicForm";
	}
	

	@RequestMapping(value = "/productPicAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productPicAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductPic productPic,
			@RequestParam MultipartFile imageFile, Model model) {

		return addSubmit(model, adminProductPicPath(productPic.getPdId()), () -> {
			String uploadPath = buildUploadPath(PRODUCTPIC_IMAGE_FOLDER);
			productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			productPic.setPdPicture(PRODUCTPIC_WEB_PATH + productPic.getPdImage());
			productPicService.add(productPic);
		});
	}

	@RequestMapping(value = "/productPicUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String productPicUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductPic productPic,
			@RequestParam MultipartFile imageFile,
			@RequestParam(required = false) String originalImage, Model model) {

		return updateSubmit(model, adminProductPicPath(productPic.getPdId()), () -> {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				productPic.setPdImage(originalImage);
			} else {
				String uploadPath = buildUploadPath(PRODUCTPIC_IMAGE_FOLDER);
				commonService.deleteUploadedFiles(originalImage, uploadPath);
				productPic.setPdImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			}
			productPic.setPdPicture(PRODUCTPIC_WEB_PATH + productPic.getPdImage());
			productPicService.update(productPic);
		});
	}

	@ResponseBody
	@PostMapping(value = "/productPicDelete")
	public String productPicDelete(@SessionAttribute Employee employeeSession,
			@ModelAttribute ProductPic productPic, Model model, HttpServletRequest request) {
		
		productPic = productPicService.getData(productPic);
		productPicService.delete(productPic);
		
		return "editProductPic?pdId="+productPic.getPdId();
	}
	
}
