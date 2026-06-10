package com.holidaydessert.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@RequestMapping("/admin/ticket")
@SessionAttributes("employeeSession")
@CrossOrigin
@Hidden
public class TicketManagement {

}
