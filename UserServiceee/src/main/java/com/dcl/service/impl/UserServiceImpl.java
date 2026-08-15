package com.dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dcl.dto.ProfileDto;
import com.dcl.dto.RoleDto;
import com.dcl.dto.UserDto;
import com.dcl.entity.Profile;
import com.dcl.entity.Role;
import com.dcl.entity.User;
import com.dcl.exception.AppException;
import com.dcl.repo.UserRepo;
import com.dcl.request.RegisterRequest;
import com.dcl.request.loginRequest;
import com.dcl.service.MailSender;
import com.dcl.service.ProfileService;
import com.dcl.service.RoleService;
import com.dcl.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private RoleService rservice;
	
	@Autowired
	private ProfileService pservice;
	
	@Autowired
	private MailSender mservice;

	@Transactional
	@Override
	public UserDto register(RegisterRequest request) {
		// TODO Auto-generated method stub
		RoleDto existingRole =rservice.getRoleByRoleName(request.getRoleName());
		
		if(existingRole == null) {
			throw new AppException("role not found", HttpStatus.NOT_FOUND);
		}
		
		User u = urepo.findByEmail(request.getEmail()).orElse(null);
		
		if(u != null) {
			throw new AppException("user already exists!",HttpStatus.CONFLICT);
		}
		
		User newUser=mapper.map(request,User.class);
		Role r = mapper.map(existingRole,Role.class);
		newUser.setRole(r);
		newUser = urepo.save(newUser);	
		
		Profile p = mapper.map(request, Profile.class);
		p.setUser(newUser);
		p=pservice.addProfile(p);
		
		mservice.sendMail(request.getEmail(),"sucessfully account created",request.getFirstName()+" "+request.getLastName());
		
		UserDto dto = mapper.map(newUser,UserDto.class);
		ProfileDto pdto = mapper.map(p,ProfileDto.class);
		dto.setProfileDto(pdto);
		dto.setRoleDto(existingRole);
		
		return dto;
	}

	
	@Override
	public UserDto login(loginRequest request) {
		// TODO Auto-generated method stub
		User alreadyExists=urepo.findByEmail(request.getEmail()).orElseThrow(()->new AppException("User Not Found!", HttpStatus.NOT_FOUND));
		
		if(!alreadyExists.getPassword().equals(request.getPassword())) {
			throw new AppException("incorrect password!", HttpStatus.BAD_REQUEST);
		}
		
		mservice.sendMail(request.getEmail(),"Happy Shopping!","logged in sucessfully!");
		
		UserDto dto = mapper.map(alreadyExists, UserDto.class);
		ProfileDto pdto = pservice.getByProfileId(dto.getUserId());
		RoleDto rdto = rservice.getRoleById(alreadyExists.getRole().getRoleId());
		dto.setProfileDto(pdto);
		dto.setRoleDto(rdto);
		
		return dto;
	}


	@Override
	public UserDto getUserById(Integer userId) {
		// TODO Auto-generated method stub
		 User u =urepo.findById(userId).orElseThrow(() -> new AppException("no user found!",HttpStatus.NOT_FOUND));
		 ProfileDto pdto = pservice.getProflieByUserId(userId);
		 RoleDto rdto = rservice.getRoleById(u.getRole().getRoleId());
		 
		 UserDto dto = mapper.map(u,UserDto.class);
		 dto.setProfileDto(pdto);
		 dto.setRoleDto(rdto);
		return dto;
	}


//	@Override
//	public List<UserDto> gettAllUsers() {
//		// TODO Auto-generated method stub
//		return urepo.findAll().stream().map(u->mapper.map(u, UserDto.class)).collect(Collectors.toList());
//	}


	@Override
	public void deleteByUserId(Integer userId) {
		// TODO Auto-generated method stub
		User u =urepo.findById(userId).orElseThrow(() -> new AppException("no user found!",HttpStatus.NOT_FOUND));
        pservice.deleteProfile(u.getProfile().getProfileId());
        urepo.deleteById(userId);
        mservice.sendMail(u.getEmail(),"Account Deletion","your account was deleted successfully!");
	}

}
