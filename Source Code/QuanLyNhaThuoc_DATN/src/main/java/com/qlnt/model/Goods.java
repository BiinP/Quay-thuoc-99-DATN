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
@Table(name = "goods")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Float quiDoi;
	private Float giaBan;
	private Boolean active = true;
	private String ghiChu;
	private Boolean banOnline = false;
	@ManyToOne @JoinColumn(name = "util_id")
	private Util util;
	@ManyToOne @JoinColumn(name = "product_id")
	private Product product;
}
