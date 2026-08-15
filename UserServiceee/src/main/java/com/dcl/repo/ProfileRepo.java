package com.dcl.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dcl.entity.Profile;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProfileRepo extends JpaRepository<Profile, Integer>{
	
	Optional<Profile> findByUserUserId(Integer userId);

}
