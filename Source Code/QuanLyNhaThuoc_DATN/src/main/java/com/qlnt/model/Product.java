package com.qlnt.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String soDangKy;
	private String donviGoc;
	private Boolean rx = false;
	private Boolean active= true;
	private String ghiChu;
	private String photo = "photo.jpg";
	@ManyToOne @JoinColumn(name = "sub_category_id")
	private SubCategory subCategory;
	@ManyToOne @JoinColumn(name = "brand_id")
	private Brand brand;
	@OneToMany(mappedBy = "product")
	private List<Goods> goods;
	@OneToMany(mappedBy = "product")
	private List<PromotionDetail> promotionDetails;
	@OneToMany(mappedBy = "product")
	private List<InputDetail> inputDetails;
}
