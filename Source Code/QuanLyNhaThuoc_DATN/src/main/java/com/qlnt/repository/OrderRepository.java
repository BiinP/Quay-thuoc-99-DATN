package com.qlnt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Order;
import com.qlnt.model.report.BestSeller;
import com.qlnt.model.report.CustomerVipInMonth;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
	@Query("SELECT o FROM Order o WHERE o.account.email = ?1")
	List<Order> findByAccount(String email);
	@Query("SELECT o FROM Order o WHERE (?1 IS NULL OR o.id = ?1) ORDER BY o.ngayTao DESC")
	Page<Order> findAllForOrderMgmt(Integer id, Pageable pageable);
	@Query("SELECT SUM(o.thanhTien) FROM Order o WHERE o.thanhCong = true AND o.ngayThanhCong = CURRENT_DATE")
	Float findCostToday();
	@Query("SELECT SUM(o.thanhTien) FROM Order o WHERE o.ngayTao = ?1")
	Float findCostToday(Date date);
	@Query("SELECT SUM(o.thanhTien) FROM Order o WHERE o.ngayTao = ?1")
	Float findCostTodayLastMonth(Date date);
	@Query("SELECT COUNT(o.id) FROM Order o WHERE o.ngayTao = CURRENT_DATE")
	Integer findOrderToday();
	@Query("SELECT COUNT(o.id) FROM Order o WHERE MONTH(o.ngayThanhCong) = MONTH(CURRENT_DATE)-1")
	Integer countOrderInLastMonth();
	@Query("SELECT COUNT(o.id) FROM Order o WHERE MONTH(o.ngayThanhCong) = MONTH(CURRENT_DATE)")
	Integer countOrderInMonth();
	@Query("SELECT SUM(o.thanhTien) FROM Order o WHERE MONTH(o.ngayThanhCong) = MONTH(CURRENT_DATE)-1 AND o.thanhCong = true")
	Float sumCostInLastMonth();
	@Query("SELECT SUM(o.thanhTien) FROM Order o WHERE MONTH(o.ngayThanhCong) = MONTH(CURRENT_DATE) AND o.thanhCong = true")
	Float sumCostInMonth();
	@Query("SELECT COUNT(o.id) FROM Order o WHERE o.doiXacNhan = true")
	Integer countOrderConfirm();
	@Query("SELECT COUNT(o.id) FROM Order o WHERE o.dangGiaoHang = true")
	Integer countOrderShipping();
	@Query("SELECT new CustomerVipInMonth(o.account.id, COUNT(o.account.id)) FROM Order o WHERE o.thanhCong = true GROUP BY o.account.id ORDER BY COUNT(o.account.id) DESC")
	Page<CustomerVipInMonth> reportCustomerVipInMonth(Pageable pageble);
}
