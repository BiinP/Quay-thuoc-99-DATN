package com.qlnt.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
	@Id
	private String id;
	private String name;
	private Boolean active = true;
	@JsonIgnore
	@OneToMany(mappedBy = "category")
	private List<SubCategory> subCategories;
}
