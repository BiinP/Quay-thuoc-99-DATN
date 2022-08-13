package com.qlnt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Goods;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Integer>{
	@Query("SELECT o FROM Goods o WHERE o.id LIKE ?1 ORDER BY o.id ASC")
	public Page<Goods> findAllById(String kw, Pageable currentPage);
	@Query("SELECT o FROM Goods o WHERE o.product.id = ?1")
	List<Goods> findByProductId (Integer id);
	@Query("SELECT o FROM Goods o "
			+ "WHERE"
			+ "	o.active = true "
			+ "	AND o.product.active = true "
			+ "	AND (:validLstBrandId IS NULL OR o.product.brand.id IN (:lstBrandId)) "
			+ "	AND (:subCateId IS NULL OR o.product.subCategory.id = :subCateId) "
			+ " AND (:giaThap IS NULL OR :giaCao IS NULL OR (o.giaBan >= :giaThap AND o.giaBan < :giaCao))")
	Page<Goods> findForProductList(@Param("validLstBrandId") String validLstBrandId, @Param("lstBrandId") List<String> lstBrandId, @Param("subCateId") String subCateId, @Param("giaThap") Float giaThap, @Param("giaCao") Float giaCao, Pageable pageable);
	@Query("SELECT o FROM Goods o WHERE o.product.subCategory.id = ?1")
	Page<Goods> findRelatedGoodsBySubCate (String id, Pageable pageable);
	@Query("SELECT o FROM Goods o WHERE o.product.name LIKE ?1")
	Page<Goods> findForSearch (String kw, Pageable pageable);
	
//	@Query("SELECT TOP ?1 FROM Goods o WHERE ")
//	@Query("SELECT COUNT(o) "true
//			+ "FROM Goods o INNER JOIN Product a"
//			+ "	ON o.product.id = a.id "
//			+ "GROUP BY a.id "
//			+ "HAVING a.id = ?1")
//	public Optional<Integer> existInGoods (Integer id);
}
