package com.qlnt.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Float soLuong;
	private Float donGia;
	private Float giamGia;
	private String ghiChu;
	@ManyToOne @JoinColumn(name = "order_id")
	private Order order;
	@ManyToOne @JoinColumn(name = "input_detail_id")
	private InputDetail inputDetail;
	
}
