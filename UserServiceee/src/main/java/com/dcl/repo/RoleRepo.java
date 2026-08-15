package com.dcl.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dcl.entity.Role;
import java.util.List;
import java.util.Optional;

import com.dcl.enums.RoleType;


@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByRoleName(RoleType roleName);

}
