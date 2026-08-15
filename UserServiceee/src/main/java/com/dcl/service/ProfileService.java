package com.dcl.service;

import org.springframework.web.multipart.MultipartFile;

import com.dcl.dto.ProfileDto;
import com.dcl.entity.Profile;
import com.dcl.request.profileUpdateRequest;

public interface ProfileService {
	
	Profile addProfile(Profile profile);
	
	ProfileDto updateProfile(Integer profileId,profileUpdateRequest request,MultipartFile image);
    
	void deleteProfile(Integer profileId);
	
	ProfileDto getByProfileId(Integer profileId);
	
	ProfileDto getProflieByUserId(Integer userId);
}
