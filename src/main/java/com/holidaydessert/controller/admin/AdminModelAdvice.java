package com.holidaydessert.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.holidaydessert.model.Authority;
import com.holidaydessert.model.Employee;
import com.holidaydessert.service.AuthorityService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice(basePackages = "com.holidaydessert.controller.admin")
public class AdminModelAdvice {

	@Autowired
    private AuthorityService authorityService;
	
    @ModelAttribute
    public void addAuthorityList(Model model, HttpSession session) {
        Object employee = session.getAttribute("employeeSession");
        // 尚未登入或 Session 不存在時，不查詢權限
        if (!(employee instanceof Employee employeeSession)) {
            return;
        }
        Authority authority = new Authority();
        authority.setEmpId(employeeSession.getEmpId());
		List<Map<String, Object>> authorityList = authorityService.list(authority);
        model.addAttribute("authorityList", authorityList);
    }
}
