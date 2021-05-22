package com.microservice.user.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.user.entity.Department;
import com.microservice.user.entity.User;
import com.microservice.user.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping
	public User save(@RequestBody User user) {
		log.info("Saving user");
		return userRepo.save(user);
		
	}

	@GetMapping
	public Map<String, Object> get(@RequestParam("id")Long id) {
		
		Map<String, Object> map = new HashMap<>();
		Optional<User> user = userRepo.findById(id);
		map.put("user", user);
		
		
		//initially url http://localhost:8091/department?id=1 
		//But it will b hard to manage port and url if we have thousands of microservice so We should service name in url
		// so we changed but after changing we got the error 500 beacuse we need to add load balancer at client side
		// So we added @LoadBalancer annotation then its started working
		//http://department-service/department?id=1//
		Department department = restTemplate.getForObject("http://department-service/department?id=1", Department.class);
		map.put("Department", department);
		return map;
		
	}

	
}
