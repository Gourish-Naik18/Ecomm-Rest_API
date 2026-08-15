package com.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.product.dto.BrandDto;
import com.product.request.AddBrandRequest;
import com.product.request.UpdateBrandRequest;

public interface BrandService {
	
	BrandDto addBrand(AddBrandRequest request,MultipartFile image);
	
	BrandDto getById(Integer brandId);
	
	List<BrandDto> getAllBrands();
	
	void deleteBrandById(Integer brandId);
	
	BrandDto updateBrand(Integer brandId,UpdateBrandRequest request,MultipartFile image);

}
