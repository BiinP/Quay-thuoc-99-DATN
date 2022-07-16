package com.qlnt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promotion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
	@Id
	private String id;
	@Temporal(TemporalType.DATE)
	private Date ngayTao = new Date();
	@Temporal(TemporalType.DATE)
	private Date ngayBatDau;
	@Temporal(TemporalType.DATE)
	private Date ngayKetThuc;
	private String moTa;
	private String photo = "photo.jpg";
	@OneToMany(mappedBy = "promotion")
	private List<PromotionDetail> promotionDetails;
}
