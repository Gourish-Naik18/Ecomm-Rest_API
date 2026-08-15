package com.dcl.request;

import com.dcl.enums.RoleType;

import lombok.Data;

@Data
public class AddRoleRequest {
	
	private RoleType roleName;

}
