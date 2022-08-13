package com.qlnt.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.qlnt.model.Input;
import com.qlnt.repository.InputRepository;
import com.qlnt.service.InputService;

@Service
public class InputServiceImpl implements InputService {
	@Autowired
	private InputRepository inputRepo;

	@Override
	public Input save(Input T) {
		// TODO Auto-generated method stub
		return inputRepo.save(T);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Page<Input> findAll(Optional<Integer> kw, Optional<Integer> currentPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Input findById(Integer id) {
		// TODO Auto-generated method stub
		return inputRepo.findById(id).get();
	}

	@Override
	public Boolean existById(Integer id) {
		// TODO Auto-generated method stub
		return inputRepo.existsById(id);
	}

	@Override
	public Page<Input> findAllByKeyword(Optional<String> kw, Optional<Integer> currentPage) {
		String keyword = kw.orElse("");
		Pageable page = PageRequest.of(currentPage.orElse(0), 10);
		if(keyword.equals("")) {
			return inputRepo.findAll(page);
		}else {
			return inputRepo.findAllByNgayTao(keyword, page);			
		}
	}

	@Override
	public List<Input> findAll() {
		// TODO Auto-generated method stub
		return inputRepo.findAll();
	}


}
