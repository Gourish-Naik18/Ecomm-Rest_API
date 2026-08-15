package com.dcl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcl.dto.RoleDto;
import com.dcl.request.AddRoleRequest;
import com.dcl.response.ApiResponse;
import com.dcl.service.RoleService;


@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private RoleService rservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> addRole(@RequestBody AddRoleRequest request){
		RoleDto dto = rservice.addRole(request.getRoleName());
		return ResponseEntity.ok(new ApiResponse<>("role added sucessfully!",dto,HttpStatus.OK));
	}

}
