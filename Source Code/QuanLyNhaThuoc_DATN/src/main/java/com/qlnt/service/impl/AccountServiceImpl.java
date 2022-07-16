package com.qlnt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Account;
import com.qlnt.repository.AccountRepository;
import com.qlnt.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{
	@Autowired AccountRepository accountRepo;

	@Override
	public Account save(Account T) {
		return accountRepo.save(T);
	}

	@Override
	public void deleteById(String id) {
		accountRepo.deleteById(id);
	}

	@Override
	public Page<Account> findAll(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		return accountRepo.findAll("%"+keyword+"%", page);
	}

	@Override
	public Account findById(String id) {
		return accountRepo.findById(id).get();
	}

	@Override
	public Boolean existById(String id) {
		return accountRepo.existsById(id);
	}

	@Override
	public Boolean existInOrderOrInput(String id) {
		Integer existInOrder = accountRepo.existInOrder(id).orElse(0);
		Integer existInInput = accountRepo.existInInput(id).orElse(0);
		if(existInOrder >= 1 || existInInput >= 1) {
			return true;
		}
		return false;
	}
	
	
}
