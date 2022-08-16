package com.qlnt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.qlnt.model.Account;
import com.qlnt.model.Role;
import com.qlnt.service.AccountService;

@Controller
public class LoginController {
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/login")
	public String loginForm() {
		return "account/login";
	}
	@GetMapping("/login/success")
	public String loginSuccess(Model model) {
		model.addAttribute("success", "Đăng nhập thành công");
		model.addAttribute("error", null);
		return "account/login";
	}
	@GetMapping("/login/error")
	public String loginError(Model model) {
		model.addAttribute("success", null);
		model.addAttribute("error", "Đăng nhập thất bại");
		return "account/login";
	}
	
	@GetMapping("/login/denied")
	public String loginDenied(Model model) {
		model.addAttribute("success", null);
		model.addAttribute("error", "Bạn không có quyền truy cập");
		return "account/login";
	}
	@GetMapping("/logout/success")
	public String logoutSuccess(Model model) {
		return "redirect:/";
	}
	@GetMapping("/register")
	public String register(@ModelAttribute Account account,
			Model model) {
		return "account/register";
	}
	@PostMapping("/process-register")
	public String processRegister(@ModelAttribute Account account,
			Model model) {
		if(accountService.existById(account.getEmail())) {
			model.addAttribute("error", "Đã tồn tại tài khoản có email "+account.getEmail());
			return "account/register";
		}else {
			
			account.setDiem((float) 0);
			Role role = new Role();
			role.setId("customer");
			account.setRole(role);
//			account.setDiaChi("");
			accountService.save(account);
			model.addAttribute("success", "Đăng ký tài khoản thành công");
			return "account/register";
		}
	}
	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "account/forgot-password";
	}
	@PostMapping("/process-forgot-password")
	public String processForgotPassword() {
		
		return "account/forgot-password";
	}
}
