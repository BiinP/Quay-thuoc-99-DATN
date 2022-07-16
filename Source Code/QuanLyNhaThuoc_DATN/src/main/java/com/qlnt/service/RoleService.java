package com.qlnt.service;

import java.util.List;

import com.qlnt.model.Role;

public interface RoleService extends BaseService<Role, String>{
	List<Role> findAll();
}
