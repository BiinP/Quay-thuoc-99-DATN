package com.qlnt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "input_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Float soLuong;
	private Float donGia;
	private Float giamGia;
	@Column(name = "vat")
	private Float VAT;
	@ManyToOne @JoinColumn(name = "input_id")
	private Input input;
	@ManyToOne @JoinColumn(name = "product_id")
	private Product product;
	@JsonIgnore
	@OneToMany(mappedBy = "inputDetail")
	private List<OrderDetail> orderDetails;
	@JsonIgnore
	@OneToMany(mappedBy = "inputDetail")
	private List<ReturnDetail> returnDetails;
}
