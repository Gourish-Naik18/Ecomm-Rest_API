package com.dcl.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.dcl.dto.RoleDto;
import com.dcl.entity.Role;
import com.dcl.enums.RoleType;
import com.dcl.exception.AppException;
import com.dcl.repo.RoleRepo;
import com.dcl.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepo rrepo;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public RoleDto addRole(RoleType roleName) {
		// TODO Auto-generated method stub
		Role r=rrepo.findByRoleName(roleName).orElse(null);
		
		if(r != null) {
			throw new AppException("role already exists!", HttpStatus.CONFLICT);
		}
		r = new Role();
		r.setRoleName(roleName);
		r = rrepo.save(r);
		return mapper.map(r, RoleDto.class);
	}

	@Override
	public RoleDto getRoleByRoleName(RoleType roleName) {
		// TODO Auto-generated method stub
		Role r = rrepo.findByRoleName(roleName).orElseThrow(() -> new AppException("no role found!", HttpStatus.NOT_FOUND));
		return mapper.map(r, RoleDto.class);
	}

	@Override
	public RoleDto getRoleById(Integer roleId) {
		// TODO Auto-generated method stub
		Role r = rrepo.findById(roleId).orElseThrow(() -> new AppException("no role found", HttpStatus.NOT_FOUND));
		return mapper.map(r, RoleDto.class);
	}

}
