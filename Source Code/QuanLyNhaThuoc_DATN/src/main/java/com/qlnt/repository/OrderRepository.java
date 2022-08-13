package com.qlnt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query("SELECT o FROM Order o WHERE o.account.email = ?1")
	List<Order> findByAccount(String email);
	@Query("SELECT o FROM Order o WHERE (?1 IS NULL OR o.id = ?1) ORDER BY o.ngayTao DESC")
	Page<Order> findAllForOrderMgmt(Integer id, Pageable pageable);
}
