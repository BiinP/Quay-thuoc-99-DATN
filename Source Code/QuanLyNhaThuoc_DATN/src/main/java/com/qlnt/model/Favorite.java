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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Temporal(TemporalType.DATE)
	private Date ngayThich = new Date();
	@ManyToOne @JoinColumn(name = "account_id")
	private Account account;
	@ManyToOne @JoinColumn(name = "goods_id")
	private Goods goods;
}
