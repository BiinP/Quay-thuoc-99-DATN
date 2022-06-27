package com.qlnt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/brand")
public class BrandContrller {
	@GetMapping("")
	public String index() {
		return "brand/list";
	}
}
