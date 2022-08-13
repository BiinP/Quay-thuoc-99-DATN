package com.qlnt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, String>{
	@Query("SELECT o FROM SubCategory o WHERE o.id LIKE ?1 OR o.name LIKE ?1 ORDER BY o.name ASC")
	public Page<SubCategory> findAll(String kw, Pageable currentPage);
	@Query("SELECT COUNT(o) "
			+ "FROM Product o INNER JOIN SubCategory s "
			+ "ON o.subCategory.id = s.id "
			+ "GROUP BY s.id "
			+ "HAVING s.id = ?1")
	public Optional<Integer> existInProduct(String id);
	@Query("SELECT o FROM SubCategory o "
			+ "WHERE o.category.id = ?1")
	public List<SubCategory> findByCategoryId(String id);
}
