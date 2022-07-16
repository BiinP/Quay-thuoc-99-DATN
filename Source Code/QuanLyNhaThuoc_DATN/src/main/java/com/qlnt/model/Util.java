package com.qlnt.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "util")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Util {
	@Id
	private String id;
	private String name;
	private String ghiChu;
	@OneToMany(mappedBy = "util")
	private List<Goods> goods;
}
