package com.qlnt.api;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qlnt.service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportAPI {
	@Autowired
	private ReportService reportService;

	@GetMapping("/information")
	public Map<String, Object> getInformation() {
		return reportService.getInformation();
	}

	@GetMapping("/cost-today")
	public Map<String, Object> getCostAndOrder() throws ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("costToday", reportService.getCostToday());
		map.put("orderToday", reportService.getOrderToday());
		map.put("costInMonth", reportService.getCostInMonth());
		return map;
	}

	@GetMapping("/best-seller")
	public Map<String, Object> getBestSeller() {
		return reportService.getBestSeller();
	}

	@GetMapping("/customer-vip")
	public Map<String, Object> getCustomerVip() {
		return reportService.getCustomerVip();
	}

	@GetMapping("/customer-vip-inmonth")
	public Map<String, Object> getCustomerVipInMonth() {
		return reportService.getCustomerVipInMonth();
	}
}
