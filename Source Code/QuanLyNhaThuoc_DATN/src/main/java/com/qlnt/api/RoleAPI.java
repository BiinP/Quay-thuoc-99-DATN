package com.qlnt.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qlnt.model.Role;
import com.qlnt.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleAPI {
	@Autowired private RoleService roleService;
	
	@GetMapping
	public List<Role> getAll(){
		return roleService.findAll();
	}
}
