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
import com.holidaydessert.model.CompanyInformation;
import com.holidaydessert.model.Department;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CompanyInformationService;
import com.holidaydessert.service.DepartmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/admin/company")
@SessionAttributes("employeeSession")
@CrossOrigin
@Api(tags = "公司管理")
public class CompanyManagement {

	@Value("${web.path}")
	private String WEB_PATH;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private CompanyInformationService companyInformationService;
	
	@Autowired
	private DepartmentService departmentService;

	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@ApiOperation(value = "公司清單", httpMethod = "GET", notes = "進行公司查詢")
	public String list(@SessionAttribute("employeeSession") Employee employeeSession, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/company/list";

	}
	
	@GetMapping("/companyTables")
	public void companyTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		CompanyInformation companyInformationData = new CompanyInformation();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		companyInformationData.setStart(start);
		companyInformationData.setLength(length);
		companyInformationData.setSearchText(searchValue);
		
		List<Map<String, Object>> companyList = companyInformationService.list(companyInformationData);

		if (companyList == null) {
			companyList = new ArrayList<Map<String, Object>>();
		}

		int count = companyInformationService.getCount(companyInformationData);

		companyInformation.setRecordsFiltered(count);
		companyInformation.setRecordsTotal(count);
		companyInformation.setData(companyList);
		companyInformation.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(companyInformation);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@GetMapping("/departmentTables")
	public void departmentTables(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Department department, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Department departmentData = new Department();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		departmentData.setStart(start);
		departmentData.setLength(length);
		departmentData.setSearchText(searchValue);
		
		List<Map<String, Object>> departmentList = departmentService.list(departmentData);

		if (departmentList == null) {
			departmentList = new ArrayList<Map<String, Object>>();
		}

		int count = departmentService.getCount(departmentData);

		department.setRecordsFiltered(count);
		department.setRecordsTotal(count);
		department.setData(departmentList);
		department.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(department);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/addCompany" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addCompany(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			CompanyInformation companyInformation = new CompanyInformation();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("companyInformation", companyInformation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/company/companyForm";
	}
	
	@RequestMapping(value = "/updateCompany" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateCompany(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			companyInformation = companyInformationService.getData(companyInformation);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("companyInformation", companyInformation);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/company/companyForm";
	}

	@RequestMapping(value = "/companyAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			companyInformationService.add(companyInformation);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/company/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/companyUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			companyInformationService.update(companyInformation);
			model.addAttribute("PATH", "/holidayDessert/admin/company/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/addDepartment" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addDepartment(@SessionAttribute("employeeSession") Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			Department department = new Department();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("department", department);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/company/departmentForm";
	}
	
	@RequestMapping(value = "/updateDepartment" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateDepartment(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Department department, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			department = departmentService.getData(department);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("department", department);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/company/departmentForm";
	}

	@RequestMapping(value = "/departmentAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String departmentAddSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Department department,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			departmentService.add(department);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/company/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/departmentUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String departmentUpdateSubmit(@SessionAttribute("employeeSession") Employee employeeSession,
			@ModelAttribute Department department,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			departmentService.update(department);
			model.addAttribute("PATH", "/holidayDessert/admin/company/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
}
