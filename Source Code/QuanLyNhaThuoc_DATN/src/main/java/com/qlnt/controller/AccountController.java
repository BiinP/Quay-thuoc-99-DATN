package com.qlnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
	@GetMapping("/login")
	public String login() {
		return "account/login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "account/register";
	}

	@GetMapping("/forgot")
	public String forgot() {
		return "account/forgot-password";
	}
	
	@GetMapping("")
	public String detail() {
		return "account/profile";
	}
	
	@GetMapping("change-password")
	public String changePassword() {
		return "account/change-pw";
	}
	
	@GetMapping("/order")
	public String myOrder() {
		return "order/list";
	}
	
	@GetMapping("/wishlist")
	public String myWishlist() {
		return "account/wishlist";
	}
}
