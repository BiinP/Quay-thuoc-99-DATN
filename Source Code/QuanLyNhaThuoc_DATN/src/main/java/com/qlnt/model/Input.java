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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "input")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Input {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Temporal(TemporalType.DATE)
	private Date ngayTao = new Date();
	private Float tongTien;
	private Float tongGiamGia;
	@Column(name = "tong_vat")
	private Float tongVAT;
	private Float thanhTien;
	private String ghiChu;
	@ManyToOne @JoinColumn(name = "account_id")
	private Account account;
	@OneToMany(mappedBy = "input")
	private List<InputDetail> inputDetails;
}
