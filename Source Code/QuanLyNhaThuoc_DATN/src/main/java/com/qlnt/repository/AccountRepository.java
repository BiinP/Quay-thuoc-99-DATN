package com.qlnt.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Account;
import com.qlnt.model.Role;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>{
	@Query("SELECT o FROM Account o WHERE o.email LIKE ?1 OR o.hoTen LIKE ?1 ORDER BY o.hoTen ASC")
	public Page<Account> findAll(String kw, Pageable currentPage);	
	@Query("SELECT COUNT(o) "
			+ "FROM Order o INNER JOIN Account a"
			+ "	ON o.account.email = a.email "
			+ "GROUP BY a.email "
			+ "HAVING a.email = ?1")
	public Optional<Integer> existInOrder (String id);
	@Query("SELECT COUNT(o) "
			+ "FROM Input o INNER JOIN Account a"
			+ "	ON o.account.email = a.email "
			+ "GROUP BY a.email "
			+ "HAVING a.email = ?1")
	public Optional<Integer> existInInput (String id);
	@Query("SELECT COUNT(o.email) FROM Account o WHERE o.role.id = 'customer'")
	Integer countCustomer();
	@Query("SELECT COUNT(o.email) FROM Account o WHERE o.role.id = 'customer' AND o.ngayTao = CURRENT_DATE")
	Integer countCustomerInMonth();
}
