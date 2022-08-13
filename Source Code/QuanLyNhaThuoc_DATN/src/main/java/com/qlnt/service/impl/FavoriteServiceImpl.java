package com.qlnt.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.qlnt.model.Account;
import com.qlnt.model.Favorite;
import com.qlnt.model.Goods;
import com.qlnt.model.InputDetail;
import com.qlnt.model.Product;
import com.qlnt.model.Promotion;
import com.qlnt.model.PromotionDetail;
import com.qlnt.repository.FavoriteRepository;
import com.qlnt.repository.PromotionDetailRepository;
import com.qlnt.service.FavoriteService;

@Service
public class FavoriteServiceImpl implements FavoriteService{
	@Autowired
	private FavoriteRepository favoriteRepo;
	@Autowired
	private PromotionDetailRepository promoRepo;
	
	@Override
	public Favorite save(Favorite T) {
		// TODO Auto-generated method stub
		return favoriteRepo.save(T);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		favoriteRepo.deleteById(id);
	}

	@Override
	public Page<Favorite> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Favorite findById(Integer id) {
		// TODO Auto-generated method stub
		return favoriteRepo.findById(id).get();
	}

	@Override
	public Boolean existById(Integer id) {
		// TODO Auto-generated method stub
		return favoriteRepo.existsById(id);
	}

	@Override
	public Map<String, Boolean> like(Integer id, Principal principal) {
		String email = principal.getName();
		List<Favorite> favs = favoriteRepo.findByAccount(email);
		boolean exist = false;
		Integer favId = 0;
		for(Favorite f : favs) {
			if(f.getGoods().getId().equals(id)) {
				exist = true;
				favId = f.getId();
			}
		}
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if(exist) {
			favoriteRepo.deleteById(favId);
			map.put("exist", true);
		}else {
			Favorite favorite = new Favorite();
			Account account = new Account();
			account.setEmail(email);
			favorite.setAccount(account);
			
			Goods goods = new Goods();
			goods.setId(id);
			favorite.setGoods(goods);
			
			Favorite favoriteSave = favoriteRepo.save(favorite);
			map.put("exist", false);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findByAccount(Principal principal) {
		String email = principal.getName();
		List<Favorite> favs = favoriteRepo.findByAccount(email);
		
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		if (favs.size() > 0) {
			for (Favorite fav : favs) {
				Map<String, Object> map = new HashMap<String, Object>();
				Product product = fav.getGoods().getProduct();
				PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
				map.put("goodsId", fav.getGoods().getId());
				map.put("name", product.getName());
				map.put("price", fav.getGoods().getGiaBan());
				map.put("rx", product.getRx());
				map.put("photo", product.getPhoto());
				map.put("util", fav.getGoods().getUtil().getName());
				map.put("conHang", product.getTonKho() > 0 ? true : false);
				if (promotionDetail != null) {
					Promotion promotion = promotionDetail.getPromotion();
					Integer discount = Math.round(promotionDetail.getDiscount());
					map.put("discount", discount);
					Date ngayKetThuc = promotion.getNgayKetThuc();
					String ngayKetThucString = ngayKetThuc.toString();
					map.put("ngayKetThuc", ngayKetThucString);
				} else {
					map.put("discount", 0);
					map.put("ngayKetThuc", null);
				}
				db.add(map);
			}
		}
		return db;
	}

}
