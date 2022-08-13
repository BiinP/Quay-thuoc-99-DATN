package com.qlnt.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.qlnt.model.Category;
import com.qlnt.model.SubCategory;
import com.qlnt.service.BrandService;
import com.qlnt.service.CategoryService;
import com.qlnt.service.SubCategoryService;

@Component
public class GlobalInterceptor implements HandlerInterceptor{
	@Autowired SubCategoryService subCateService;
	@Autowired CategoryService cateService;
	@Autowired BrandService bService;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		List<Category> categories = cateService.findAll();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for(Category cate : categories) {
			List<SubCategory> subCates = subCateService.findByCategoryId(cate.getId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("category", cate.getName());
			map.put("subCates", subCates);
			db.add(map);
		}
		request.setAttribute("categories", db);
		request.setAttribute("brands", bService.findAll());
	}
}
