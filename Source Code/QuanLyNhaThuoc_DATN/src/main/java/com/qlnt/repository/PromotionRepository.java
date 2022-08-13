package com.qlnt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qlnt.model.Product;
import com.qlnt.model.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, String>{
	@Query("SELECT o FROM Promotion o WHERE o.id LIKE ?1 ORDER BY o.ngayTao DESC")
	public Page<Promotion> findAllByKeyword(String kw, Pageable currentPage);
	@Query("SELECT o "
			+ "FROM Promotion o "
			+ "WHERE o.ngayBatDau < CURRENT_DATE "
			+ "	AND o.ngayKetThuc >= CURRENT_DATE"
			+ "	ORDER BY o.ngayKetThuc ASC")
	public List<Promotion> findPromotionAvailble();
}
