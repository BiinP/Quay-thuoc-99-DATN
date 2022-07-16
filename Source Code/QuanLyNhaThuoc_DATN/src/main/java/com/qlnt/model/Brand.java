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
@Table(name = "brand")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
	@Id
	private String id;
	private String name;
	private String xuatXu;
	private String photo = "photo.jpg";
	private String ghiChu;
	private Boolean active = true;
	@OneToMany(mappedBy = "brand")
	private List<Product> products;
}
