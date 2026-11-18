package com.holidaydessert.controller.admin;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.holidaydessert.model.CompanyInformation;
import com.holidaydessert.model.Department;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.CompanyInformationService;
import com.holidaydessert.service.DepartmentService;

import io.swagger.v3.oas.annotations.Hidden;
import static com.holidaydessert.constant.BuildPath.*;

@Controller
@RequestMapping("/admin/company")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class CompanyManagement {
	
	@Autowired
	private CompanyInformationService companyInformationService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, Model model) throws Exception {
		
		return "admin/company/list";
	}
	
	@ResponseBody
	@GetMapping("/companyTables")
	public Map<String, Object> companyTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return companyInformationService.list(params);
	}
	
	@ResponseBody
	@GetMapping("/departmentTables")
	public Map<String, Object> departmentTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return departmentService.list(params);
	}

	@RequestMapping(value = "/addCompany" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addCompany(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		try {
			CompanyInformation companyInformation = new CompanyInformation();
			model.addAttribute("companyInformation", companyInformation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/company/companyForm";
	}
	
	@RequestMapping(value = "/updateCompany" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateCompany(@SessionAttribute Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation, Model model) throws Exception {
		
		try {
			companyInformation = companyInformationService.getData(companyInformation);
			model.addAttribute("companyInformation", companyInformation);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/company/companyForm";
	}

	@RequestMapping(value = "/companyAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyAddSubmit(@SessionAttribute Employee employeeSession,
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
		model.addAttribute("PATH", ADMIN_COMPANY_LIST);

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/companyUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			companyInformationService.update(companyInformation);
			model.addAttribute("PATH", ADMIN_COMPANY_LIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/addDepartment" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addDepartment(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		try {
			Department department = new Department();
			model.addAttribute("department", department);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/company/departmentForm";
	}
	
	@RequestMapping(value = "/updateDepartment" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateDepartment(@SessionAttribute Employee employeeSession,
			@ModelAttribute Department department, Model model) throws Exception {
		
		try {
			department = departmentService.getData(department);
			model.addAttribute("department", department);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/company/departmentForm";
	}

	@RequestMapping(value = "/departmentAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String departmentAddSubmit(@SessionAttribute Employee employeeSession,
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
		model.addAttribute("PATH", ADMIN_COMPANY_LIST);

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/departmentUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String departmentUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Department department,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			departmentService.update(department);
			model.addAttribute("PATH", ADMIN_COMPANY_LIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
}
