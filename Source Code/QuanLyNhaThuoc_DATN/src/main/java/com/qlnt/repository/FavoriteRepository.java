package com.qlnt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
	@Query("SELECT o FROM Favorite o WHERE o.account.email = ?1")
	List<Favorite> findByAccount(String email);
}
