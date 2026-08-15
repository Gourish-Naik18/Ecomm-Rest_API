package com.dcl.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dcl.dto.ProfileDto;
import com.dcl.entity.Profile;
import com.dcl.exception.AppException;
import com.dcl.repo.ProfileRepo;
import com.dcl.request.profileUpdateRequest;
import com.dcl.response.CloudinaryResponse;
import com.dcl.service.CloudinaryService;
import com.dcl.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {
	
	@Autowired
	private ProfileRepo prepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CloudinaryService cservice;

	@Override
	public Profile addProfile(Profile profile) {
		// TODO Auto-generated method stub
		return prepo.save(profile);
	}

	@Override
	public ProfileDto updateProfile(Integer profileId, profileUpdateRequest request, MultipartFile image) {
		// TODO Auto-generated method stub
		Profile p = prepo.findById(profileId).orElseThrow(()-> new AppException("no profile found!", HttpStatus.NOT_FOUND));
        mapper.map(request, p);
        
        if(image != null && !image.isEmpty()) {
        if(p.getImageUrl() != null && p.getPublicUrl() != null) {
        	cservice.deleteImage(p.getPublicUrl());
        }
        CloudinaryResponse response=cservice.uploadImage(image);
        p.setImageUrl(response.getImageUrl());
        p.setPublicUrl(response.getPublicId());
        }
        p = prepo.save(p);
		return mapper.map(p, ProfileDto.class);
	}

	@Override
	public void deleteProfile(Integer profileId) {
		// TODO Auto-generated method stub
		prepo.findById(profileId).orElseThrow(() -> new AppException("no profile found!", HttpStatus.NOT_FOUND));
        prepo.deleteById(profileId);
	}

	@Override
	public ProfileDto getByProfileId(Integer profileId) {
		// TODO Auto-generated method stub
		Profile p = prepo.findById(profileId).orElseThrow(()-> new AppException("no profile found!", HttpStatus.NOT_FOUND));
		return mapper.map(p, ProfileDto.class);
	}

	@Override
	public ProfileDto getProflieByUserId(Integer userId) {
		// TODO Auto-generated method stub
		Profile p = prepo.findByUserUserId(userId).orElseThrow(() -> new AppException("no profile found!", HttpStatus.NOT_FOUND));
		return mapper.map(p, ProfileDto.class);
	}

}
