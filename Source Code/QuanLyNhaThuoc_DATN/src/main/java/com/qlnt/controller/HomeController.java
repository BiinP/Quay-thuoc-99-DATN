package com.qlnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
	
	@GetMapping("/")
	public String index() {
		return "home/index";
	}
	@GetMapping("/promotion")
	public String promotion() {
		return "promotion/list";
	}
}
