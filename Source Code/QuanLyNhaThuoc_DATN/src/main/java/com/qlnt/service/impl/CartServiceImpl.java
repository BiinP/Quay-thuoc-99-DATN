package com.qlnt.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qlnt.model.Goods;
import com.qlnt.model.Product;
import com.qlnt.model.Promotion;
import com.qlnt.model.PromotionDetail;
import com.qlnt.repository.PromotionDetailRepository;
import com.qlnt.service.CartService;
import com.qlnt.service.GoodsService;
import com.qlnt.service.ProductService;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private ProductService productService;
	@Autowired
	private GoodsService goodsService;
	@Autowired 
	private PromotionDetailRepository promoRepo;
	
	@Override
	public Map<String, Object> add(Integer id) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Goods goods = goodsService.findById(id);
		map.put("goods", goods);
		Product product = goods.getProduct();
		PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
		if (promotionDetail != null) {
			Promotion promotion = promotionDetail.getPromotion();
			Integer discount = Math.round(promotionDetail.getDiscount());
			map.put("discount", discount);
		} else {
			map.put("discount", 0);
		}
		return map;
	}

}
