package com.microservice.department.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.department.entity.Department;
import com.microservice.department.repo.DepartmentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	@PostMapping
	public Department save(@RequestBody Department department) {
		log.info("Saving department");
		return departmentRepository.save(department);
		
	}

	@GetMapping
	public Optional<Department> get(@RequestParam("id")Long id) {
		return departmentRepository.findById(id);
		
	}

	
}
