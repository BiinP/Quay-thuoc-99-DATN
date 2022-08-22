package com.qlnt.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.qlnt.model.Account;
import com.qlnt.model.Goods;
import com.qlnt.model.report.BestSeller;
import com.qlnt.model.report.CustomerVipInMonth;
import com.qlnt.repository.AccountRepository;
import com.qlnt.repository.GoodsRepository;
import com.qlnt.repository.OrderDetailRepository;
import com.qlnt.repository.OrderRepository;
import com.qlnt.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderDetailRepository odRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private GoodsRepository goodsRepository;

	@Override
	public Float getCostToday() {
		// TODO Auto-generated method stub
		return orderRepository.findCostToday()==null?0:orderRepository.findCostToday();
	}

	@Override
	public Integer getOrderToday() {
		// TODO Auto-generated method stub
		return orderRepository.findOrderToday()==null?0:orderRepository.findOrderToday();
	}

	@Override
	public Map<String, Object> getInformation() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sumCustomer", accountRepository.countCustomer());
		map.put("cusInMonth", accountRepository.countCustomerInMonth());
		map.put("countOrderInLastMonth", orderRepository.countOrderInLastMonth());
		map.put("countOrderInMonth", orderRepository.countOrderInMonth());
		map.put("sumCostInLastMonth", orderRepository.sumCostInLastMonth());
		map.put("sumCostInMonth", orderRepository.sumCostInMonth());
		map.put("countOrderConfirm", orderRepository.countOrderConfirm());
		map.put("countOrderShipping", orderRepository.countOrderShipping());
		return map;
	}

	@Override
	public Map<String, Object> getBestSeller() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> lstProductName = new ArrayList<String>();
		List<Long> lstQuantity = new ArrayList<Long>();

		Pageable pageable = PageRequest.of(0, 5);
		Page<BestSeller> lstBestSeller = odRepository.reportBestSeller(pageable);
		for (BestSeller bestSeller : lstBestSeller.getContent()) {
			Integer goodsId = bestSeller.getGoodsId();
			Goods goods = goodsRepository.findById(goodsId).get();

			lstProductName.add(goods.getProduct().getName());
			lstQuantity.add(bestSeller.getCount());
		}
		map.put("lstProductName", lstProductName);
		map.put("lstQuantity", lstQuantity);
		return map;
	}

	@Override
	public Map<String, Object> getCustomerVip() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> lstAccountName = new ArrayList<String>();
		List<Float> lstDiem = new ArrayList<Float>();

		Sort sort = Sort.by(Direction.DESC, "diem");
		List<Account> accounts = accountRepository.findAll(sort);
		for (int i = 0; i < 10; i++) {
			Account account = accounts.get(i);
			lstAccountName.add(account.getEmail());
			lstDiem.add(account.getDiem());
		}
		map.put("lstAccountName", lstAccountName);
		map.put("lstDiem", lstDiem);
		return map;
	}

	@Override
	public Map<String, Object> getCustomerVipInMonth() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> lstAccountName = new ArrayList<String>();
		List<Long> lstDiem = new ArrayList<Long>();

		Pageable pageable = PageRequest.of(0, 10);
		Page<CustomerVipInMonth> lstCusInMonth = orderRepository.reportCustomerVipInMonth(pageable);
		for (CustomerVipInMonth customer : lstCusInMonth.getContent()) {
			lstAccountName.add(customer.getAccountId());
			lstDiem.add(customer.getCount());
		}
		map.put("lstAccountName", lstAccountName);
		map.put("lstDiem", lstDiem);
		return map;
	}

	public List<Integer> getDays() {
		List<Integer> lstDay = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		Date currentDate = new Date();
		cal.set(Calendar.MONTH, currentDate.getMonth());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		SimpleDateFormat df = new SimpleDateFormat("dd");
		for (int i = 0; i < maxDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i + 1);
//			System.out.println(df.format(cal.getTime()));
			Integer day = Integer.valueOf(df.format(cal.getTime()));
			lstDay.add(day);
		}
		return lstDay;
	}

	@Override
	public List<Map<String, Object>> getCostInMonth() throws ParseException {
		List<Map<String, Object>> db = new ArrayList<Map<String,Object>>();
		List<Integer> lstDay = this.getDays();
		Date currentDate = new Date();
		Integer year = currentDate.getYear();
		Integer month = currentDate.getMonth();
		for (Integer day : lstDay) {
			Map<String, Object> map = new HashMap<String, Object>();
			
			Date date = new Date(year, month, day);
			Float cost = orderRepository.findCostToday(date)==null?0:orderRepository.findCostToday(date);
			map.put("d", day);
			map.put("inMonth", cost);
			
			Date dateLastMonth = new Date(year, month-1, day);
			Float costLastMonth = orderRepository.findCostTodayLastMonth(dateLastMonth)==null?0:orderRepository.findCostTodayLastMonth(dateLastMonth);
			map.put("lastMonth", costLastMonth);
			db.add(map);
		}
		return db;
	}

}
