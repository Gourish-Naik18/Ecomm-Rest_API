package com.dcl.service;

import com.dcl.dto.RoleDto;
import com.dcl.enums.RoleType;

public interface RoleService {
	
	RoleDto addRole(RoleType roleName);
	
	RoleDto getRoleByRoleName(RoleType roleName);
	
	RoleDto getRoleById(Integer roleId);
	

}
