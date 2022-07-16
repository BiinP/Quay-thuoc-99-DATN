package com.qlnt.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "promotion_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Float discount;
	@ManyToOne @JoinColumn(name = "promotion_id")
	private Promotion promotion;
	@ManyToOne @JoinColumn(name = "product_id")
	private Product product;
}
