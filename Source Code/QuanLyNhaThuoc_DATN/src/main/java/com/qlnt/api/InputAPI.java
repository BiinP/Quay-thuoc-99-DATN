package com.qlnt.api;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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
import com.qlnt.model.InputDetail;
import com.qlnt.model.Input;
import com.qlnt.model.Product;
import com.qlnt.service.InputDetailService;
import com.qlnt.service.InputService;

@RestController
@RequestMapping("/api/inputs")
public class InputAPI {
	@Autowired
	private InputService inputService;
	@Autowired
	private InputDetailService inputDetailService;

	@GetMapping
	public Page<Input> findAll(@RequestParam("kw") Optional<String> kw,
			@RequestParam("currentPage") Optional<Integer> currentPage) {
		return inputService.findAllByKeyword(kw, currentPage);
	}

	@PostMapping
	public ResponseEntity<Input> createInput(@RequestBody Input input) {
		if (inputService.existById(input.getId())) {
			return ResponseEntity.badRequest().build();
		} else {
			return ResponseEntity.ok(inputService.save(input));
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<Input> updateInput(@PathVariable("id") Integer id, @RequestBody Input input) {
		if (!inputService.existById(input.getId())) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(inputService.save(input));
		}
	}

	@GetMapping("/{id}")
	public HashMap<String, Object> findById(@PathVariable("id") Integer id) {
//		ObjectMapper mapper = new ObjectMapper();
//		ObjectNode node = mapper.createObjectNode();
//
		Input input = inputService.findById(id);
//		JsonNode inputJSON = mapper.convertValue(input, JsonNode.class);
//		node.put("input", inputJSON);
//
		List<InputDetail> inputDetails = inputDetailService.findAllByInputId(id);
//		JsonNode inputDetailsJSON = mapper.convertValue(inputDetails, JsonNode.class);
//		node.put("inputDetails", inputDetailsJSON);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("input", input);
		map.put("inputDetails", inputDetails);
		return map;
	}

	@GetMapping("/detail/{id}")
	public ResponseEntity<InputDetail> findInputDetail(@PathVariable("id") Integer id) {
		if (inputDetailService.existById(id)) {
			return ResponseEntity.ok(inputDetailService.findById(id));

		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/detail")
	public InputDetail createInputDetail(@RequestBody InputDetail inputDetail) {
		return inputDetailService.save(inputDetail);
	}
}
