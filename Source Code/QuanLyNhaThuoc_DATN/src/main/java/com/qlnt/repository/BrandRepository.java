package com.qlnt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Account;
import com.qlnt.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, String>{
	@Query("SELECT o FROM Brand o WHERE o.id LIKE ?1 OR o.name LIKE ?1 ORDER BY o.name ASC")
	public Page<Brand> findAll(String kw, Pageable currentPage);
	@Query("SELECT o FROM Brand o WHERE o.active = true ORDER BY o.name ASC")
	List<Brand> findAllForListBrand(); 
	@Query("SELECT o FROM Brand o WHERE o.active = true ORDER BY o.name ASC")
	Page<Brand> findAllForListBrand(Pageable pageable); 
	@Query("SELECT COUNT(o) "
			+ "FROM Product o INNER JOIN Brand b"
			+ "	ON o.brand.id = b.id "
			+ "GROUP BY b.id "
			+ "HAVING b.id = ?1")
	public Optional<Integer> existInProduct(String id);
}
