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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.model.Product;
import com.holidaydessert.model.ProductCollection;
import com.holidaydessert.model.ProductPic;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.ProductCollectionService;
import com.holidaydessert.service.ProductPicService;
import com.holidaydessert.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/product")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "商品管理")
public class ProductManagement {

	@Value("${web.path}")
	private String WEB_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCollectionService productCollectionService;
	
	@Autowired
	private ProductPicService productPicService;
	
	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "商品清單", httpMethod = "GET", notes = "進行商品查詢")
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
	
}
