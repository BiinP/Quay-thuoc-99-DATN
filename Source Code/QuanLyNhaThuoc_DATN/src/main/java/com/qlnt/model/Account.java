package com.qlnt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
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
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	@Id
	private String email;
	private String hoTen;
	private Boolean gioiTinh = true;
	@Temporal(TemporalType.DATE)
	private Date ngaySinh;
	private String sdt;
	@Temporal(TemporalType.DATE)
	private Date ngayTao = new Date();
	private Float diem = (float) 0.0;
	private String matKhau;
	private String avatar = "avatar.png";
	private String ghiChu;
	private Boolean active = true;
	@ManyToOne @JoinColumn(name = "role_id")
	private Role role;
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	private List<Order> orders;
	@JsonIgnore
	@OneToMany(mappedBy = "account")
	private List<Input> inputs;
}
