package com.dcl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcl.dto.UserDto;
import com.dcl.exception.AppException;
import com.dcl.request.RegisterRequest;
import com.dcl.request.loginRequest;
import com.dcl.response.ApiResponse;
import com.dcl.service.UserService;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
	
	@Autowired
	private UserService uservice;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request,BindingResult result){
		
		if(result.hasErrors()) {
			throw new AppException(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		UserDto dto = uservice.register(request); 
		return ResponseEntity.ok(new ApiResponse<>("registered successfully",dto,HttpStatus.OK));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody loginRequest request){
		UserDto dto = uservice.login(request);
		return ResponseEntity.ok(new ApiResponse<>("login successfull!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> delete(@PathVariable Integer userId){
		uservice.deleteByUserId(userId);
		return ResponseEntity.ok(new ApiResponse("delete sucessfull!", null, HttpStatus.OK));
	}
	
	@GetMapping("/get/{userId}")
	public ResponseEntity<?> getByID(@PathVariable Integer userId){
		 UserDto dto=uservice.getUserById(userId);
		 return ResponseEntity.ok(new ApiResponse("user data",dto,HttpStatus.OK));
	}
	
//	@GetMapping("/getAll")
//	public ResponseEntity<?> getAll(){
//		 List<UserDto> li = uservice.gettAllUsers();
//		 return ResponseEntity.ok(new ApiResponse("user data",li,HttpStatus.OK));
//	}
	
	
	
	
	
	
	
	
	
	
	
	

}
