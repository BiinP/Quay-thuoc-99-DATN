package com.qlnt.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.qlnt.model.Role;
import com.qlnt.repository.RoleRepository;
import com.qlnt.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	@Autowired private RoleRepository roleRepo;
	
	@Override
	public Role save(Role T) {
		return roleRepo.save(T);
	}

	@Override
	public void deleteById(String id) {
		roleRepo.deleteById(id);
	}

	@Override
	public Page<Role> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		return null;
	}
	
	@Override
	public List<Role> findAll(){
		return roleRepo.findAll();
	}

	@Override
	public Role findById(String id) {
		return roleRepo.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		return roleRepo.existsById(id);
	}

}
