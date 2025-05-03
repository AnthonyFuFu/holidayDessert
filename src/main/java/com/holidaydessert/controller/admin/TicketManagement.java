package com.holidaydessert.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/admin/ticket")
@SessionAttributes("employeeSession")
@CrossOrigin
@ApiIgnore
public class TicketManagement {

}
