package com.microservice.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

	
	private Long departmentId;
	private String depName;
	private String depAddress;
	private String depCode;
	
}
