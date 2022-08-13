package com.qlnt.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Util;

@Repository
public interface UtilRepository extends JpaRepository<Util, String>{
	@Query("SELECT o FROM Util o WHERE o.id LIKE ?1 OR o.name LIKE ?1 ORDER BY o.id ASC")
	public Page<Util> findAll(String kw, Pageable currentPage);
	@Query("SELECT COUNT(o) "
			+ "FROM Goods o INNER JOIN Util u"
			+ "	ON o.util.id = u.id "
			+ "GROUP BY u.id "
			+ "HAVING u.id = ?1")
	public Optional<Integer> existInGoods (String id);
}
