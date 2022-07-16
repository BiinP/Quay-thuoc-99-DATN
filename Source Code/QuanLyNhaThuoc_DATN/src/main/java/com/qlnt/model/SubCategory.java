package com.qlnt.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategory {
	@Id
	private String id;
	private String name;
	private String photo = "photo.jpg";
	private Boolean active = true;
	@ManyToOne() @JoinColumn(name = "category_id")
	private Category category;
	@OneToMany(mappedBy = "subCategory")
	private List<Product> products;
}
