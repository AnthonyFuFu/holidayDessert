package com.holidaydessert.controller.admin;

import java.util.Map;

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
import static com.holidaydessert.utils.ControllerUtil.*;

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
	public String addCompany(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("companyInformation", new CompanyInformation());
		return "admin/company/companyForm";
	}
	
	@RequestMapping(value = "/updateCompany" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateCompany(@SessionAttribute Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation, Model model) {

		model.addAttribute("companyInformation", companyInformationService.getData(companyInformation));
		return "admin/company/companyForm";
	}

	@RequestMapping(value = "/companyAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation, Model model) {

		return addSubmit(model, ADMIN_COMPANY_LIST, () -> companyInformationService.add(companyInformation));
	}
	
	@RequestMapping(value = "/companyUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String companyUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute CompanyInformation companyInformation, Model model) {

		return updateSubmit(model, ADMIN_COMPANY_LIST, () -> companyInformationService.update(companyInformation));
	}
	
	@RequestMapping(value = "/addDepartment" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addDepartment(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("department", new Department());
		return "admin/company/departmentForm";
	}
	
	@RequestMapping(value = "/updateDepartment" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateDepartment(@SessionAttribute Employee employeeSession,
			@ModelAttribute Department department, Model model) {

		model.addAttribute("department", departmentService.getData(department));
		return "admin/company/departmentForm";
	}

	@RequestMapping(value = "/departmentAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String departmentAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Department department, Model model) {

		return addSubmit(model, ADMIN_COMPANY_LIST, () -> departmentService.add(department));
	}
	
	@RequestMapping(value = "/departmentUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String departmentUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Department department, Model model) {

		return updateSubmit(model, ADMIN_COMPANY_LIST, () -> departmentService.update(department));
	}
	
}
