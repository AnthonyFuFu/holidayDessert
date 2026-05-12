package com.holidaydessert.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.holidaydessert.model.Authority;
import com.holidaydessert.model.EmpFunction;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;
import com.holidaydessert.service.CommonService;
import com.holidaydessert.service.DepartmentService;
import com.holidaydessert.service.EmpFunctionService;
import com.holidaydessert.service.EmployeeService;
import static com.holidaydessert.constant.BuildPath.*;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/admin/employee")
@SessionAttributes("employeeSession")
@CrossOrigin
@ApiIgnore
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
	
	private Gson gson = new Gson();
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@SessionAttribute Employee employeeSession, @ModelAttribute Employee employee, Model model, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		model.addAttribute("authorityList", authorityList);
		return "admin/employee/list";

	}
	
	@GetMapping("/employeeTables")
	public void employeeTables(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Employee employeeData = new Employee();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		employeeData.setStart(start);
		employeeData.setLength(length);
		employeeData.setSearchText(searchValue);
		
		List<Map<String, Object>> employeeList = employeeService.list(employeeData);

		if (employeeList == null) {
			employeeList = new ArrayList<Map<String, Object>>();
		}

		int count = employeeService.getCount(employeeData);

		employee.setRecordsFiltered(count);
		employee.setRecordsTotal(count);
		employee.setData(employeeList);
		employee.setDraw(Integer.valueOf(draw));

		String output = gson.toJson(employee);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/authorityTables")
	public void authorityTables(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		Employee employeeData = new Employee();
		Authority authority = new Authority();
		
		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		employeeData.setStart(start);
		employeeData.setLength(length);
		employeeData.setSearchText(searchValue);

		List<Map<String, Object>> employeeList = employeeService.list(employeeData);

		if (employeeList == null) {
			employeeList = new ArrayList<Map<String, Object>>();
		}

		for(int i=0; i<employeeList.size(); i++) {
			authority.setEmpId(employeeList.get(i).get("EMP_ID").toString());
			List<Map<String, Object>> authList = authorityService.getAuthorityList(authority);
			employeeList.get(i).put("AUTH_LIST", authList);
		}
		
		int count = employeeService.getCount(employeeData);
		
		employee.setRecordsFiltered(count);
		employee.setRecordsTotal(count);
		employee.setData(employeeList);
		employee.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(employee);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@GetMapping("/empFunctionTables")
	public void empFunctionTables(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction, HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		EmpFunction empFunctionData = new EmpFunction();

		String start = pRequest.getParameter("start") == null ? "0" : pRequest.getParameter("start");
		String length = pRequest.getParameter("length") == null ? "10" : pRequest.getParameter("length");
		String draw = pRequest.getParameter("draw") == null ? "0" : pRequest.getParameter("draw");
		String searchValue = pRequest.getParameter("search[value]") == null ? "" : pRequest.getParameter("search[value]");

		empFunctionData.setStart(start);
		empFunctionData.setLength(length);
		empFunctionData.setSearchText(searchValue);
		
		List<Map<String, Object>> empFunctionList = empFunctionService.list(empFunctionData);

		if (empFunctionList == null) {
			empFunctionList = new ArrayList<Map<String, Object>>();
		}

		int count = empFunctionService.getCount(empFunctionData);

		empFunction.setRecordsFiltered(count);
		empFunction.setRecordsTotal(count);
		empFunction.setData(empFunctionList);
		empFunction.setDraw(Integer.valueOf(draw));
		
		String output = gson.toJson(empFunction);

		pResponse.setCharacterEncoding("utf-8");
		
		try {
			PrintWriter out;
			out = pResponse.getWriter();
			out.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping(value = "/addEmployee" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addEmployee(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		List<Map<String, Object>> departmentList = departmentService.getList();
		try {
			Employee employee = new Employee();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("departmentList", departmentList);
			model.addAttribute("employee", employee);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/employee/employeeForm";
	}
	
	@RequestMapping(value = "/updateEmployee" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateEmployee(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		List<Map<String, Object>> departmentList = departmentService.getList();
		
		try {
			employee = employeeService.getData(employee);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("departmentList", departmentList);
			model.addAttribute("employee", employee);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/employee/employeeForm";
	}
	
	@RequestMapping(value = "/employeeAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String employeeAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee,
			@RequestParam MultipartFile imageFile,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
	        String uploadPath = buildUploadPath(EMPLOYEE_IMAGE_FOLDER);
	        employee.setEmpImage(commonService.saveByDateNameUploadedFiles(imageFile, uploadPath));
	        employee.setEmpPicture(EMPLOYEE_WEB_PATH + employee.getEmpImage());
			employeeService.add(employee);
			
			String id = employeeService.getNextId();
			employee.setEmpId(id);
			
			List<Map<String, Object>> adminList = empFunctionService.getAdminListToAuth();
			if (employee.getEmpLevel().equals("0")) {
				authorityService.addAdminAuthority(employee, adminList);
			} else {
				authorityService.addStaffAuthority(employee, adminList);
			}
			
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/employee/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/employeeUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String employeeUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee,
			@RequestParam MultipartFile imageFile,
			@RequestParam(required = false) String originalImage,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
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
			model.addAttribute("PATH", "/holidayDessert/admin/employee/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@PostMapping("/resignEmployee")
	public String resignEmployee(@SessionAttribute Employee employeeSession,
			@ModelAttribute Employee employee, Model model) throws Exception {

		try {
			employeeService.resign(employee);
			model.addAttribute("PATH", "/holidayDessert/admin/employee/list");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
	}
	
	@RequestMapping(value = "/addEmpFunction" , method = {RequestMethod.GET, RequestMethod.POST})
	public String addEmpFunction(@SessionAttribute Employee employeeSession,
			HttpServletRequest pRequest, HttpServletResponse pResponse, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			EmpFunction empFunction = new EmpFunction();
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("empFunction", empFunction);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/employee/empFunctionForm";
	}
	
	@RequestMapping(value = "/updateEmpFunction" , method = {RequestMethod.GET, RequestMethod.POST})
	public String updateEmpFunction(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction, Model model) throws Exception {
		
		// 權限
		Authority authority = new Authority();
		authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
		
		try {
			empFunction = empFunctionService.getData(empFunction);
			model.addAttribute("authorityList", authorityList);
			model.addAttribute("empFunction", empFunction);
			model.addAttribute("MESSAGE", "資料修改成功");
		} catch (JSONException e) {
			model.addAttribute("MESSAGE", "修改失敗，請重新操作");
			e.printStackTrace();
		}
		return "admin/employee/empFunctionForm";
	}
	
	@RequestMapping(value = "/empFunctionAddSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String empFunctionAddSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction,
			HttpServletRequest pRequest, Model model) throws Exception {

		try {
			empFunctionService.add(empFunction);
			model.addAttribute("MESSAGE", "資料新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("MESSAGE", "新增失敗，請重新操作");
			throw new Exception("dataRollback");
		}
		model.addAttribute("PATH", "/holidayDessert/admin/employee/list");

		return "admin/toPath";
	}
	
	@RequestMapping(value = "/empFunctionUpdateSubmit" , method = {RequestMethod.GET, RequestMethod.POST})
	public String empFunctionUpdateSubmit(@SessionAttribute Employee employeeSession,
			@ModelAttribute EmpFunction empFunction,
			HttpServletRequest pRequest, Model model) throws Exception {
		
		try {
			empFunctionService.update(empFunction);
			model.addAttribute("PATH", "/holidayDessert/admin/employee/list");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "admin/toPath";
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
