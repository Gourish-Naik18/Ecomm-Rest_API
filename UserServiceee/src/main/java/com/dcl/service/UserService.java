package com.dcl.service;

import java.util.List;

import com.dcl.dto.UserDto;
import com.dcl.request.RegisterRequest;
import com.dcl.request.loginRequest;

public interface UserService {
	
	UserDto register(RegisterRequest request);
	
	UserDto login(loginRequest request);
	
	UserDto getUserById(Integer userId);
	
//	List<UserDto> gettAllUsers();
	
	void deleteByUserId(Integer userId);
	
	

}
