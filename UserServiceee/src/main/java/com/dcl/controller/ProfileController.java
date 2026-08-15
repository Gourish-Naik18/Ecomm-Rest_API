package com.dcl.controller;


import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dcl.dto.ProfileDto;
import com.dcl.request.profileUpdateRequest;
import com.dcl.response.ApiResponse;
import com.dcl.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
	
	@Autowired
	private ProfileService pservice;
	
	@PutMapping(value="/update/{profileId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateProfile(@PathVariable Integer profileId,
			                              @RequestPart(value="image",required = false) MultipartFile image,
			                              @RequestParam String firstName,@RequestParam String lastName,
			                              @RequestParam LocalDate dob,@RequestParam String phone){
		profileUpdateRequest request = new profileUpdateRequest();
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setDob(dob);
		request.setPhone(phone);
		ProfileDto dto = pservice.updateProfile(profileId, request, image);
		return ResponseEntity.ok(new ApiResponse<>("updated sucessfully!",dto,HttpStatus.OK));
	}

}
