package com.qlnt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.InputDetail;

@Repository
public interface InputDetailRepository extends JpaRepository<InputDetail, Integer>{
	@Query("SELECT o FROM InputDetail o WHERE o.input.id = ?1")
	List<InputDetail> findAllByInputId(Integer id);
	@Query("SELECT DISTINCT o FROM InputDetail o GROUP BY o.product.id ORDER BY o.input.ngayTao DESC ")
	Page<InputDetail> findNewest(Pageable pageable);
	@Query("SELECT o FROM InputDetail o WHERE o.product.id = ?1 ORDER BY o.input.ngayTao ASC")
	List<InputDetail> findByProductId(Integer id);
//	@Query("SELECT COUNT(o) "
//			+ "FROM OrderDetail o INNER JOIN InputDetail a"
//			+ "	ON o.inputDetail.id = a.id "
//			+ "GROUP BY a.id "
//			+ "HAVING a.id = ?1")
//	public Optional<Integer> existInOrderDetail (Integer id);
//	@Query("SELECT COUNT(o) "
//			+ "FROM ReturnDetail o INNER JOIN InputDetail a"
//			+ "	ON o.inputDetail.id = a.id "
//			+ "GROUP BY a.id "
//			+ "HAVING a.id = ?1")
//	public Optional<Integer> existInReturnDetail (Integer id);
}
