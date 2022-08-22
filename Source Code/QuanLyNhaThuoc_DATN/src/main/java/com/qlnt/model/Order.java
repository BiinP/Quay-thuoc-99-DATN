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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Temporal(TemporalType.DATE)
	private Date ngayTao = new Date();
	@Temporal(TemporalType.DATE)
	private Date ngayThanhCong = null;
	private String sdt;
	private String diaChi;
	private Float phiGiaoHang;
	private Float tongTien;
	private Float tongGiamGia;
	private Float thanhTien;
	private Boolean doiXacNhan = true;
	private Boolean dangGiaoHang = false;
	private Boolean thanhCong = false;
	private Boolean daHuy = false;
	private Boolean daTraHang = false; 
	private Boolean taiCuaHang = false;
	private String maNV = null;
	@ManyToOne @JoinColumn(name = "account_id")
	private Account account;
	@JsonIgnore
	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;
}
