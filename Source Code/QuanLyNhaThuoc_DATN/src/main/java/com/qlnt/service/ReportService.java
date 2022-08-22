package com.qlnt.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ReportService {
	Float getCostToday();
	Integer getOrderToday();
	List<Map<String, Object>> getCostInMonth() throws ParseException;
	Map<String, Object> getInformation();
	Map<String, Object> getBestSeller();
	Map<String, Object> getCustomerVip();
	Map<String, Object> getCustomerVipInMonth();
}
