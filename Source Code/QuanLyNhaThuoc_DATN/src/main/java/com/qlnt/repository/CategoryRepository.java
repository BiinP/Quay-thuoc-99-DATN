package com.qlnt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
	@Query("SELECT o FROM Category o WHERE o.id LIKE ?1 OR o.name LIKE ?1")
	public Page<Category> findAll(String kw, Pageable currentPage);
}
