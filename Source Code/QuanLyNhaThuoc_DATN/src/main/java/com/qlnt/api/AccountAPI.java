package com.qlnt.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qlnt.model.Account;
import com.qlnt.service.AccountService;
import com.qlnt.service.UploadService;

@RestController
@RequestMapping("/api/accounts")
public class AccountAPI {
	@Autowired
	private AccountService accountService;
	@Autowired
	private UploadService uploadService;

	@GetMapping
	public Page<Account> getAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage) {
		return accountService.findAll(kw, currentPage);
	}

	@GetMapping("/{id}")
	public Account getById(@PathVariable("id") String id) {
		return accountService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Account> create(@RequestBody Account account) {
		if (accountService.existById(account.getEmail())) {
			return ResponseEntity.badRequest().build();
		}else {
			return ResponseEntity.ok(accountService.save(account));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Account> update(@PathVariable("id") String id, @RequestBody Account account){
		if (accountService.existById(id)) {
			String imgOld = accountService.findById(id).getAvatar();
			if(!account.getAvatar().equals(imgOld) && !imgOld.equals("avatar.png")) {
				uploadService.delete("account", imgOld);
			}
			return ResponseEntity.ok(accountService.save(account));
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public JsonNode delete(@PathVariable("id") String id){
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		if (accountService.existById(id)) {
			Account account = accountService.findById(id);
			if(accountService.existInOrderOrInput(id)) {
				account.setActive(false);
				accountService.save(account);
				node.put("isFound", true);
				node.put("isExist", true);
				return node;
			}else {
				accountService.deleteById(id);
				if(!account.getAvatar().equals("avatar.png")) {
					uploadService.delete("account", account.getAvatar());
				}
				node.put("isFound", true);
				node.put("isExist", false);
				return node;
			}
		}else {
			node.put("isFound", false);
			node.put("isExist", false);
			return node;
		}
	}
	
	
}
