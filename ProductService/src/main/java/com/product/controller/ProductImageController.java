package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.ProductImageDto;
import com.product.response.ApiResponse;
import com.product.service.ProductImageService;

@RestController
@RequestMapping("/productImage")
public class ProductImageController {
	
	@Autowired
	private ProductImageService iService;
	
	@PostMapping(value = "/upload/{productId}" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> uploadImage(@PathVariable Integer productId,@RequestPart("images") List<MultipartFile> images){
		List<ProductImageDto> list = iService.uploadImages(productId, images);
		return ResponseEntity.ok(new ApiResponse<>("image upload sucessfull",list,HttpStatus.OK));
	}
	
	@GetMapping("/get/{productId}")
	public ResponseEntity<?> getImageByProductId(@PathVariable Integer productId){
		List<ProductImageDto> list = iService.getImagesByProductId(productId);
		return ResponseEntity.ok(new ApiResponse<>("product images!",list,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{imageId}")
	public ResponseEntity<?> deleteImageById(@PathVariable Integer imageId){
		iService.deleteImageById(imageId);
		return ResponseEntity.ok(new ApiResponse<>("deleted sucessfully!",null,HttpStatus.OK));
	}
	
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllImages(){
		List<ProductImageDto> list = iService.getAllImages();
		return ResponseEntity.ok(new ApiResponse<>("All images and data",list,HttpStatus.OK));
	}

}
