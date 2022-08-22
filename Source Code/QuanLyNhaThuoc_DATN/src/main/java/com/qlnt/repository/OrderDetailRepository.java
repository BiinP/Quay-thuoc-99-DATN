package com.qlnt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qlnt.model.OrderDetail;
import com.qlnt.model.report.BestSeller;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	@Query("SELECT o FROM OrderDetail o GROUP BY o.goodsId ORDER BY COUNT(o.goodsId) DESC")
	List<OrderDetail> findBestSeller ();
	@Query("SELECT new BestSeller(o.goodsId, COUNT(o.goodsId)) FROM OrderDetail o GROUP BY o.goodsId ORDER BY COUNT(o.goodsId) DESC")
	Page<BestSeller> reportBestSeller(Pageable pageble);
}
