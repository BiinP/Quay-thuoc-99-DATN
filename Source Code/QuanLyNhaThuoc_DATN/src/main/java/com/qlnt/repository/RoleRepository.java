package com.qlnt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qlnt.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String>{

}
