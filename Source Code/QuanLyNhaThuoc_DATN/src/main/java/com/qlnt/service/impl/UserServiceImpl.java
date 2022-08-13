package com.qlnt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.qlnt.model.Account;
import com.qlnt.service.AccountService;
import com.qlnt.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private AccountService aService;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		try {
			Account account = aService.findById(email);
			String password = account.getMatKhau();
//			String[] roles = account.getRole().stream().
//					map(au -> au.getRole().getRole())
//					.collect(Collectors.toList()).toArray(new String[0]);
			String role = account.getRole().getId();
			return User.withUsername(email).password(pe.encode(password)).roles(role).build();
		} catch (Exception e) {
			throw new UsernameNotFoundException(email + " not found");
		}
	}

}
