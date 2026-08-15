package com.product.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.product.exception.AppException;
import com.product.response.CloudinaryResponse;
import com.product.service.CloudinaryService;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
	
	@Autowired
	private Cloudinary cloudinary;

	@Override
	public CloudinaryResponse uploadImage(MultipartFile image) {
		// TODO Auto-generated method stub
		CloudinaryResponse response = null;
		
		 try {
			if(image != null && !image.isEmpty()) {
			Map<?, ?> result=cloudinary.uploader().upload(image.getBytes(),ObjectUtils.emptyMap());
			String publicId = result.get("public_id").toString();
			String imageUrl = result.get("secure_url").toString();
			response = new CloudinaryResponse(imageUrl, publicId);
			 }
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		
		return response;
	}

	@Override
	public void deleteImage(String publicId) {
		// TODO Auto-generated method stub
		try {
			Map<?, ?> result=cloudinary.uploader().destroy(publicId,ObjectUtils.emptyMap());
			String status = result.get("result").toString();
			
			if(!status.equalsIgnoreCase("ok")) {
				throw new AppException("An error occured!!", HttpStatus.BAD_REQUEST);
			}
			
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		
		

	}

}
