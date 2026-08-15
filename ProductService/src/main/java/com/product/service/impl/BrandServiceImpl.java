package com.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.BrandDto;
import com.product.entity.Brand;
import com.product.exception.AppException;
import com.product.repo.BrandRepo;
import com.product.request.AddBrandRequest;
import com.product.request.UpdateBrandRequest;
import com.product.response.CloudinaryResponse;
import com.product.service.BrandService;
import com.product.service.CloudinaryService;

@Service
public class BrandServiceImpl implements BrandService {
	
	@Autowired
	private BrandRepo brepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CloudinaryService cservice;

	
	@Override
	public BrandDto addBrand(AddBrandRequest request,MultipartFile image) {
		// TODO Auto-generated method stub
		Brand b = brepo.findByBrandName(request.getBrandName().trim()).orElse(null);
		
		if(b != null) {
			throw new AppException("brand already exists!", HttpStatus.CONFLICT);
		}
		
		b = mapper.map(request,Brand.class);
		
		if(image != null && !image.isEmpty()) {
			CloudinaryResponse response=cservice.uploadImage(image);
			b.setImageUrl(response.getImageUrl());
			b.setPublicUrl(response.getPublicId());
		}
		b = brepo.save(b);
		return mapper.map(b,BrandDto.class);
	}

	
	
	@Override
	public BrandDto getById(Integer brandId) {
		// TODO Auto-generated method stub
		Brand b = brepo.findById(brandId).orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));		
		return mapper.map(b,BrandDto.class);
	}



	@Override
	public List<BrandDto> getAllBrands() {
		// TODO Auto-generated method stub
		List<Brand> list = brepo.findAll();
		return list.stream().map((b)->mapper.map(b,BrandDto.class)).collect(Collectors.toList());
	}



	@Override
	public void deleteBrandById(Integer brandId) {
		// TODO Auto-generated method stub
		Brand b=brepo.findById(brandId).orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
		
		if(b.getProducts() != null && !b.getProducts().isEmpty()) {
			throw new AppException("brand cannot be deleted !", HttpStatus.BAD_REQUEST);
		}
		
		if(b.getImageUrl() != null && b.getPublicUrl() != null) {
			cservice.deleteImage(b.getPublicUrl());
		}
		
		brepo.deleteById(brandId);
	}



	@Override
	public BrandDto updateBrand(Integer brandId, UpdateBrandRequest request, MultipartFile image) {
		// TODO Auto-generated method stub
	   Brand b=brepo.findById(brandId).orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
       
	   Brand b1 = brepo.findByBrandName(request.getBrandName().trim()).orElse(null);
		
		if(b1 != null && !b1.getBrandId().equals(brandId)) {
			throw new AppException("brand already exists!", HttpStatus.CONFLICT);
		}
	   
       	mapper.map(request,b);
       
       if(image != null && !image.isEmpty()) {
			if(b.getImageUrl() != null && b.getPublicUrl() != null) {
				cservice.deleteImage(b.getPublicUrl());
			}
			CloudinaryResponse response=cservice.uploadImage(image);
			b.setImageUrl(response.getImageUrl());
			b.setPublicUrl(response.getPublicId());
		}

        b=brepo.save(b);
		return mapper.map(b,BrandDto.class);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
