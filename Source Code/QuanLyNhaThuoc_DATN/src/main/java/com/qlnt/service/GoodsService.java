package com.qlnt.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.qlnt.model.Goods;

public interface GoodsService extends BaseService<Goods, Integer>{
	Page<Goods> findAllByKeyword (Optional<String> kw, Optional<Integer> currentPage);
	List<Goods> findByProductId (Integer id);
	List<Map<String, Object>> findGoodsForListProduct (String brandId, String subCateId, Integer range, Integer page, String sort);
	List<Map<String, Object>> findGoodsForSaleOf ();
	List<Map<String, Object>> findGoodsForNewest ();
	Map<String, Object> findDetailGoods (Integer id, Principal principal);
	List<Map<String, Object>> findRelatedGoodsBySubCate (String id);
	List<Map<String, Object>> findGoodsForSearch(String kw, Integer page);
	List<Map<String, Object>> findGoodsForBestSeller();
	
}
