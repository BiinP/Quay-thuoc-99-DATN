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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@JsonIgnore
	@OneToMany(mappedBy = "goods")
	private List<Favorite> favorites;
}
