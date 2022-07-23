package com.qlnt.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Account;
import com.qlnt.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	@Query("SELECT o FROM Product o WHERE o.id LIKE ?1 ORDER BY o.id ASC")
	public Page<Product> findAllById(String kw, Pageable currentPage);
	@Query("SELECT o FROM Product o WHERE o.name LIKE ?1 ORDER BY o.id ASC")
	public Page<Product> findAllByName(String kw, Pageable currentPage);
	@Query("SELECT COUNT(o) "
			+ "FROM Goods o INNER JOIN Product a"
			+ "	ON o.product.id = a.id "
			+ "GROUP BY a.id "
			+ "HAVING a.id = ?1")
	public Optional<Integer> existInGoods (Integer id);
	@Query("SELECT COUNT(o) "
			+ "FROM InputDetail o INNER JOIN Product a"
			+ "	ON o.product.id = a.id "
			+ "GROUP BY a.id "
			+ "HAVING a.id = ?1")
	public Optional<Integer> existInInputDetail (Integer id);
}
