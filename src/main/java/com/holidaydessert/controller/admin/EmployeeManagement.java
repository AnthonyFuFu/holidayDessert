package com.holidaydessert.controller.admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.EmpFunction;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.DepartmentService;
import com.holidaydessert.service.EmpFunctionService;
import com.holidaydessert.service.EmployeeService;
import io.swagger.v3.oas.annotations.Hidden;
import static com.holidaydessert.constant.BuildPath.*;
import static com.holidaydessert.utils.ControllerUtil.*;

@Controller
@RequestMapping("/admin/employee")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class EmployeeManagement {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmpFunctionService empFunctionService;

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, @ModelAttribute Employee employee, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		return "admin/employee/list";
	}
	
	@ResponseBody
	@GetMapping("/employeeTables")
	public Map<String, Object> employeeTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return employeeService.list(params);
	}

	@ResponseBody
	@GetMapping("/authorityTables")
	public Map<String, Object> authorityTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return employeeService.authorityList(params);
	}
	
	@ResponseBody
	@GetMapping("/empFunctionTables")
	public Map<String, Object> empFunctionTables(@SessionAttribute Employee employeeSession, @RequestParam Map<String, String> params) {
		return empFunctionService.list(params);
	}
	
	@RequestMapping(value = "/addEmployee" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addEmployee(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("departmentList", safe("departmentList", departmentService::getList, Collections.emptyList()));
		model.addAttribute("employee", new Employee());
		return "admin/employee/employeeForm";
	}
	
	@RequestMapping(value = "/updateEmployee" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateEmployee(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee, Model model) {

		model.addAttribute("departmentList", safe("departmentList", departmentService::getList, Collections.emptyList()));
		model.addAttribute("employee", employeeService.getData(employee));
		return "admin/employee/employeeForm";
	}
	
	@RequestMapping(value = "/employeeAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String employeeAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee,
			@RequestParam MultipartFile imageFile, Model model) {

		return addSubmit(model, ADMIN_EMPLOYEE_LIST, () -> {
			String uploadPath = buildUploadPath(EMPLOYEE_IMAGE_FOLDER);
			employee.setEmpImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			employee.setEmpPicture(EMPLOYEE_WEB_PATH + employee.getEmpImage());
			employeeService.add(employee);
			
			// 依員工等級建立預設權限
			employee.setEmpId(employeeService.getNextId());
			List<Map<String, Object>> adminList = empFunctionService.getAdminListToAuth();
			if (employee.getEmpLevel().equals("0")) {
				authorityService.addAdminAuthority(employee, adminList);
			} else {
				authorityService.addStaffAuthority(employee, adminList);
			}
		});
	}
	
	@RequestMapping(value = "/employeeUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String employeeUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee,
			@RequestParam MultipartFile imageFile,
			@RequestParam(required = false) String originalImage, Model model) {

		return updateSubmit(model, ADMIN_EMPLOYEE_LIST, () -> {
			// 若原image_url 不為空且無新檔案名稱，則image_url 設為原始image_url
			if (originalImage != null && imageFile.getOriginalFilename().equals("")) {
				employee.setEmpImage(originalImage);
			} else {
				String uploadPath = buildUploadPath(EMPLOYEE_IMAGE_FOLDER);
				commonService.deleteUploadedFiles(originalImage, uploadPath);
				employee.setEmpImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
			}
			employee.setEmpPicture(EMPLOYEE_WEB_PATH + employee.getEmpImage());
			employeeService.update(employee);
		});
	}
	
	@PostMapping("/resignEmployee")
	public String resignEmployee(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee, Model model) {

		return updateSubmit(model, ADMIN_EMPLOYEE_LIST, () -> employeeService.resign(employee));
	}
	
	@RequestMapping(value = "/addEmpFunction" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addEmpFunction(@SessionAttribute Employee employeeSession, Model model) {

		model.addAttribute("empFunction", new EmpFunction());
		return "admin/employee/empFunctionForm";
	}
	
	@RequestMapping(value = "/updateEmpFunction" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateEmpFunction(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction, Model model) {

		model.addAttribute("empFunction", empFunctionService.getData(empFunction));
		return "admin/employee/empFunctionForm";
	}
	
	@RequestMapping(value = "/empFunctionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String empFunctionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction, Model model) {

		return addSubmit(model, ADMIN_EMPLOYEE_LIST, () -> empFunctionService.add(empFunction));
	}
	
	@RequestMapping(value = "/empFunctionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String empFunctionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction, Model model) {

		return updateSubmit(model, ADMIN_EMPLOYEE_LIST, () -> empFunctionService.update(empFunction));
	}
	
	@RequestMapping(value = "/authUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public void authUpdateSubmit(HttpServletRequest pRequest, HttpServletResponse pResponse, HttpSession session, Model model) {
		
		String empId = pRequest.getParameter("empId")!=null ? pRequest.getParameter("empId") : "";
		String funcId = pRequest.getParameter("funcId")!=null ? pRequest.getParameter("funcId") : "";
		String authStatus = pRequest.getParameter("authStatus")!=null ? pRequest.getParameter("authStatus") : "";
		
		Authority authority = new Authority();
		authority.setEmpId(empId);
		authority.setFuncId(funcId);
		authority.setAuthStatus(authStatus);
		
		authorityService.update(authority);
		
	}
	
}
