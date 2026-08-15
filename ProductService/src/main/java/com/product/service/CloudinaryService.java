package com.product.service;

import org.springframework.web.multipart.MultipartFile;

import com.product.response.CloudinaryResponse;


public interface CloudinaryService {
	
	CloudinaryResponse uploadImage(MultipartFile image);
	
	void deleteImage(String publicId);


}
