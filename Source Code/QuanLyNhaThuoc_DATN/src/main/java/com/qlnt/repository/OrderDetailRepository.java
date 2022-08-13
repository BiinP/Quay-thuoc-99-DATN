package com.qlnt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qlnt.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{

}
