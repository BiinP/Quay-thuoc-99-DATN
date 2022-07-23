package com.qlnt.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String>{
	@Query("SELECT o FROM Category o WHERE o.id LIKE ?1 OR o.name LIKE ?1 ORDER BY o.name ASC")
	public Page<Category> findAll(String kw, Pageable currentPage);
	@Query("SELECT COUNT(o) "
			+ "FROM SubCategory o INNER JOIN Category s "
			+ "ON o.category.id = s.id "
			+ "GROUP BY s.id "
			+ "HAVING s.id = ?1")
	public Optional<Integer> existInSubCategory(String id);
}
