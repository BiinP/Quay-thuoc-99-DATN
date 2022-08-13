package com.qlnt.service.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.qlnt.model.Favorite;
import com.qlnt.model.Goods;
import com.qlnt.model.InputDetail;
import com.qlnt.model.Product;
import com.qlnt.model.Promotion;
import com.qlnt.model.PromotionDetail;
import com.qlnt.repository.FavoriteRepository;
import com.qlnt.repository.GoodsRepository;
import com.qlnt.repository.InputDetailRepository;
import com.qlnt.repository.PromotionDetailRepository;
import com.qlnt.service.GoodsService;
import com.qlnt.service.InputDetailService;

@Service
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsRepository goodsRepo;
	@Autowired
	private PromotionDetailRepository promoRepo;
	@Autowired
	private InputDetailRepository inputDetailRepo;
	@Autowired
	private FavoriteRepository favRepo;

	@Override
	public Goods save(Goods T) {
		// TODO Auto-generated method stub
		return goodsRepo.save(T);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		goodsRepo.deleteById(id);
	}

	@Override
	public Page<Goods> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		return null;
	}

	@Override
	public Page<Goods> findAllByKeyword(Optional<String> kw, Optional<Integer> currentPage) {
//		String keyword = kw.orElse("");
//		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
//		try {
//			Integer id = Integer.valueOf(keyword);
//			return goodsRepo.findAllById("%"+id+"%", page);
//		} catch (Exception e) {
//			return productRepo.findAllByName("%"+keyword+"%", page);
//		}
		return null;
	}

	@Override
	public Goods findById(Integer id) {
		// TODO Auto-generated method stub
		return goodsRepo.findById(id).get();
	}

	@Override
	public Boolean existById(Integer id) {
		// TODO Auto-generated method stub
		return goodsRepo.existsById(id);
	}

	@Override
	public List<Goods> findByProductId(Integer id) {
		// TODO Auto-generated method stub
		return goodsRepo.findByProductId(id);
	}

	@Override
	public List<Map<String, Object>> findGoodsForListProduct(String brandId, String subCateId, Integer range,
			Integer page, String sort) {
		List<String> brandIdAsArray = new ArrayList<String>();
		try {
			if (!brandId.equals("")) {
//				brandId = null;
				String[] lst = brandId.split(",");
				brandIdAsArray = Arrays.asList(lst);
			}
			if (brandId.equals("")) {
				brandId = null;
				brandIdAsArray.add("abc");
			}
		} catch (Exception e) {
			brandId = null;
			brandIdAsArray.add("abc");
		}
		if (subCateId.equals(""))
			subCateId = null;
		Float giaThap = null, giaCao = null;
		if (range == 0) {
			giaThap = null;
			giaCao = null;
		}
		if (range == 100) {
			giaThap = (float) 0;
			giaCao = (float) 100000;
		}
		if (range == 300) {
			giaThap = (float) 100000;
			giaCao = (float) 300000;
		}
		if (range == 900) {
			giaThap = (float) 300000;
			giaCao = (float) 900000;
		}
		if (range == 1000) {
			giaThap = (float) 900000;
			giaCao = (float) 10000000;
		}
		Sort sortable = Sort.by(Direction.ASC, "id");
		if (sort.equals("lowPrice")) {
			sortable = Sort.by(Direction.ASC, "giaBan");
		}
		if (sort.equals("highPrice")) {
			sortable = Sort.by(Direction.DESC, "giaBan");
		}
		Pageable pageable = PageRequest.of(page, 16, sortable);
		Page<Goods> lstGoodsAsPage = goodsRepo.findForProductList(brandId, brandIdAsArray, subCateId, giaThap, giaCao,
				pageable);
		List<Goods> lstGoods = lstGoodsAsPage.getContent();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		if (lstGoods.size() > 0) {
			for (Goods goods : lstGoods) {
				Map<String, Object> map = new HashMap<String, Object>();
				Product product = goods.getProduct();
				PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
				map.put("totalPage", lstGoodsAsPage.getTotalPages());
				map.put("goodsId", goods.getId());
				map.put("name", product.getName());
				map.put("price", goods.getGiaBan());
				map.put("rx", product.getRx());
				map.put("photo", product.getPhoto());
				map.put("util", goods.getUtil().getName());
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
//					Date date = new Date();
//					String dateString = date.toString();
					map.put("ngayKetThuc", null);
				}
				db.add(map);
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalPage", 0);
			map.put("goodsId", "");
			map.put("name", "");
			map.put("price", 0);
			map.put("rx", false);
			map.put("photo", "");
			map.put("util", "");
			map.put("discount", 0.0);
			db.add(map);
		}
		return db;
	}

	@Override
	public List<Map<String, Object>> findGoodsForSaleOf() {
		List<Goods> lstGoods = goodsRepo.findAll();
		List<PromotionDetail> promoDetails = promoRepo.findForHomeSale();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Goods goods : lstGoods) {
//			PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
			for (PromotionDetail promoDetail : promoDetails) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (goods.getProduct().getId().equals(promoDetail.getProduct().getId())) {
					Product product = goods.getProduct();
					map.put("goodsId", goods.getId());
					map.put("name", product.getName());
					map.put("price", goods.getGiaBan());
					map.put("rx", product.getRx());
					map.put("photo", product.getPhoto());
					map.put("util", goods.getUtil().getName());
					Integer discount = Math.round(promoDetail.getDiscount());
					map.put("discount", discount);
					Date ngayKetThuc = promoDetail.getPromotion().getNgayKetThuc();
					String ngayKetThucString = ngayKetThuc.toString();
					map.put("ngayKetThuc", ngayKetThucString);
					map.put("conHang", product.getTonKho() > 0 ? true : false);
					db.add(map);
				}
			}
		}
		return db;
	}

	@Override
	public List<Map<String, Object>> findGoodsForNewest() {
		List<Goods> lstGoods = goodsRepo.findAll();
		List<InputDetail> inputDetails = inputDetailRepo.findNewest(PageRequest.of(0, 12)).getContent();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Goods goods : lstGoods) {
			for (InputDetail inputDetail : inputDetails) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (goods.getProduct().getId().equals(inputDetail.getProduct().getId())) {
					Product product = goods.getProduct();
					map.put("goodsId", goods.getId());
					map.put("name", product.getName());
					map.put("price", goods.getGiaBan());
					map.put("rx", product.getRx());
					map.put("photo", product.getPhoto());
					map.put("util", goods.getUtil().getName());
					map.put("conHang", product.getTonKho() > 0 ? true : false);
					PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
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
		}
		return db;
	}

	@Override
	public Map<String, Object> findDetailGoods(Integer id, Principal principal) {
		Goods goods = this.findById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		Product product = goods.getProduct();
		PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
		map.put("goodsId", goods.getId());
		map.put("name", product.getName());
		map.put("price", goods.getGiaBan());
		map.put("rx", product.getRx());
		map.put("photo", product.getPhoto());
		map.put("util", goods.getUtil().getName());
		map.put("moTa", product.getMoTa());
		map.put("subCategory", product.getSubCategory());
		map.put("conHang", product.getTonKho() > 0 ? true : false);
		Integer tonKho = (int) (product.getTonKho() * goods.getQuiDoi());
		map.put("tonKho", tonKho);
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
		if (principal != null) {
			String email = principal.getName();
			List<Favorite> favs = favRepo.findByAccount(email);
			if (favs.size() <= 0) {
				map.put("liked", false);
			} else {
				for (Favorite f : favs) {
					if (f.getGoods().getId().equals(id)) {
						map.put("liked", true);
						break;
					} else {
						map.put("liked", false);
					}
				}
			}
		} else {
			map.put("liked", false);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> findRelatedGoodsBySubCate(String id) {
		Pageable pageable = PageRequest.of(0, 10);
		List<Goods> lstGoods = goodsRepo.findRelatedGoodsBySubCate(id, pageable).getContent();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Goods goods : lstGoods) {
			Map<String, Object> map = new HashMap<String, Object>();
			Product product = goods.getProduct();
			PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
			map.put("goodsId", goods.getId());
			map.put("name", product.getName());
			map.put("price", goods.getGiaBan());
			map.put("rx", product.getRx());
			map.put("photo", product.getPhoto());
			map.put("util", goods.getUtil().getName());
			if (promotionDetail != null) {
				Promotion promotion = promotionDetail.getPromotion();
				Integer discount = Math.round(promotionDetail.getDiscount());
				map.put("discount", discount);
				Date ngayKetThuc = promotion.getNgayKetThuc();
				String ngayKetThucString = ngayKetThuc.toString();
				map.put("ngayKetThuc", ngayKetThucString);
			} else {
				map.put("discount", 0);
				Date date = new Date();
				String dateString = date.toString();
				map.put("ngayKetThuc", null);
			}

			db.add(map);
		}
		return db;
	}

	@Override
	public List<Map<String, Object>> findGoodsForSearch(String kw, Integer page) {
		Pageable pageable = PageRequest.of(page, 16);
		Page<Goods> pageGoods = goodsRepo.findForSearch("%" + kw + "%", pageable);
		List<Goods> lstGoods = pageGoods.getContent();
		List<PromotionDetail> promoDetails = promoRepo.findForHomeSale();
		List<Map<String, Object>> db = new ArrayList<Map<String, Object>>();
		for (Goods goods : lstGoods) {
			Map<String, Object> map = new HashMap<String, Object>();
			Product product = goods.getProduct();
			PromotionDetail promotionDetail = promoRepo.findByProductIdForListProduct(product.getId());
			map.put("totalPage", pageGoods.getTotalPages());
			map.put("goodsId", goods.getId());
			map.put("name", product.getName());
			map.put("price", goods.getGiaBan());
			map.put("rx", product.getRx());
			map.put("photo", product.getPhoto());
			map.put("util", goods.getUtil().getName());
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
		return db;
	}

}
