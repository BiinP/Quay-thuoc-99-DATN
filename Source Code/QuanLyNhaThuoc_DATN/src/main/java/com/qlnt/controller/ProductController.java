package com.qlnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {
	@GetMapping("")
	public String index() {
		return "product/list";
	}
	
	@GetMapping("/{id}")
	public String detail() {
		return "product/detail";
	}
}
