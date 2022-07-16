package com.qlnt.model;

import java.util.Date;
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
@Table(name = "return_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Float soLuong;
	private Float donGia;
	private Float giamGia;
	private String ghiChu;
	@ManyToOne @JoinColumn(name = "return_id")
	private Returns returns;
	@ManyToOne @JoinColumn(name = "input_detail_id")
	private InputDetail inputDetail;
}
