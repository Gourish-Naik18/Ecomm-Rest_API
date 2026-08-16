package com.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.product.dto.ProductImageDto;

public interface ProductImageService {
	
	List<ProductImageDto> uploadImages(Integer productId,List<MultipartFile> images);
	
	List<ProductImageDto> getImagesByProductId(Integer productId);
	
	void deleteImageById(Integer imageId);
	
	List<ProductImageDto> getAllImages();

}
