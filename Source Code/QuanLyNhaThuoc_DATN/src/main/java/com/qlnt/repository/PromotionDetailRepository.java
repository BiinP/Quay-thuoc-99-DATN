package com.qlnt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer>{
	@Query("SELECT o FROM PromotionDetail o WHERE o.promotion.id = ?1")
	List<PromotionDetail> findByPromotionId(String id);	
	@Query("DELETE FROM PromotionDetail o WHERE o.promotion.id = ?1")
	void deleteByPromotionId(String id);
	@Query("SELECT o "
			+ "FROM PromotionDetail o "
			+ "WHERE o.product.id = ?1 "
			+ "	AND o.promotion.ngayBatDau < CURRENT_DATE "
			+ "	AND o.promotion.ngayKetThuc >= CURRENT_DATE")
	PromotionDetail findByProductIdForListProduct(Integer id);
	@Query("SELECT o "
			+ "FROM PromotionDetail o "
			+ "WHERE o.promotion.ngayBatDau < CURRENT_DATE "
			+ "	AND o.promotion.ngayKetThuc >= CURRENT_DATE"
			+ "	ORDER BY o.promotion.ngayKetThuc ASC")
	List<PromotionDetail> findForHomeSale();
}
