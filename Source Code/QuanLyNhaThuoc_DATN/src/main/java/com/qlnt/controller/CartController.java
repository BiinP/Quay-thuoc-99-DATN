package com.qlnt.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qlnt.model.Account;
import com.qlnt.service.AccountService;

@Controller
@RequestMapping("/cart")
public class CartController {
	@Autowired 
	private AccountService accService;
	
	@GetMapping("/order")
	public String cart() {
		return "order/cart";
	}
	
	@GetMapping("/order/checkout")
	public String checkout(Model model, Principal principal) {
		String email = principal.getName();
		Account acc = accService.findById(email);
		model.addAttribute("account", acc);
		return "order/checkout";
	}
	
}
