package com.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.CategoryDto;
import com.product.entity.Category;
import com.product.exception.AppException;
import com.product.repo.CategoryRepo;
import com.product.request.AddCategoryRequest;
import com.product.request.UpdateCategoryRequest;
import com.product.response.CloudinaryResponse;
import com.product.service.CategoryService;
import com.product.service.CloudinaryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepo crepo;
	
	@Autowired
	private CloudinaryService cservice;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto addCategory(AddCategoryRequest request, MultipartFile image) {
		// TODO Auto-generated method stub
		Category c =crepo.findByCategoryName(request.getCategoryName().trim()).orElse(null);
		
		if(c != null) {
			throw new AppException("category already exists!",HttpStatus.CONFLICT);
		}
		
		c = mapper.map(request,Category.class);
		
		if(request.getParentCategoryName() != null && !request.getParentCategoryName().isEmpty()) {
			Category parent = crepo.findByCategoryName(request.getParentCategoryName()).orElseThrow(() -> new AppException("no parent category found!", HttpStatus.BAD_REQUEST));
			c.setParentCategory(parent);
		}
		
		if(image != null && !image.isEmpty()) {
			CloudinaryResponse response =cservice.uploadImage(image);
			c.setImageUrl(response.getImageUrl());
			c.setPublicUrl(response.getPublicId());
		}
		c = crepo.save(c);
		
		CategoryDto dto = mapper.map(c, CategoryDto.class);
		if(c.getParentCategory() != null) {
			dto.setParentCategoryId(c.getParentCategory().getCategoryId());
			dto.setParentCategoryName(c.getParentCategory().getCategoryName());
		}
		
		return dto;
	}

	@Override
	public CategoryDto getById(Integer categoryId) {
		// TODO Auto-generated method stub
		Category c = crepo.findById(categoryId).orElseThrow(()-> new AppException("no category found!",HttpStatus.NOT_FOUND));
		
		CategoryDto dto = mapper.map(c, CategoryDto.class);
		if(c.getParentCategory() != null) {
			dto.setParentCategoryId(c.getParentCategory().getCategoryId());
			dto.setParentCategoryName(c.getParentCategory().getCategoryName());
		}
		
		return dto;
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		// TODO Auto-generated method stub
		List<Category> list = crepo.findAll();
		return list.stream().map((c)->{
			CategoryDto dto = mapper.map(c,CategoryDto.class);
			if(c.getParentCategory() != null) {
				dto.setParentCategoryId(c.getParentCategory().getCategoryId());
				dto.setParentCategoryName(c.getParentCategory().getCategoryName());
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public CategoryDto UpdateCategory(Integer categoryId, UpdateCategoryRequest request, MultipartFile image) {
		// TODO Auto-generated method stub
		Category c = crepo.findById(categoryId).orElseThrow(()-> new AppException("no category found!",HttpStatus.NOT_FOUND));
		
		Category c1 = crepo.findByCategoryName(request.getCategoryName().trim()).orElse(null);
		
		if(c1 != null && !c1.getCategoryId().equals(categoryId)) {
			throw new AppException("category already exists!", HttpStatus.CONFLICT);
		}
		
		mapper.map(request,c);
		
		if(request.getParentCategoryName() != null && !request.getParentCategoryName().isEmpty()) {
			if(request.getParentCategoryName().equalsIgnoreCase(c.getCategoryName())) {
				throw new AppException("category cannot be itselfs parent!",HttpStatus.BAD_REQUEST);
			}
			Category parent = crepo.findByCategoryName(request.getParentCategoryName()).orElseThrow(() -> new AppException("no parent category found!", HttpStatus.BAD_REQUEST));
			c.setParentCategory(parent);
		}
		else {
			c.setParentCategory(null);
		}
		
		if(image != null && !image.isEmpty()) {
			if(c.getImageUrl() != null && c.getPublicUrl() != null) {
				cservice.deleteImage(c.getPublicUrl());
			}
			CloudinaryResponse response=cservice.uploadImage(image);
			c.setImageUrl(response.getImageUrl());
			c.setPublicUrl(response.getPublicId());
		}
		
		c=crepo.save(c);
		
		CategoryDto dto = mapper.map(c, CategoryDto.class);
		if(c.getParentCategory() != null) {
			dto.setParentCategoryId(c.getParentCategory().getCategoryId());
			dto.setParentCategoryName(c.getParentCategory().getCategoryName());
		}
		
		return dto;
	}

	
	@Override
	public void deleteCategoryById(Integer categoryId) {
		// TODO Auto-generated method stub
	  Category c = crepo.findById(categoryId).orElseThrow(()-> new AppException("no category found!",HttpStatus.NOT_FOUND));

		if(c.getProducts() != null && !c.getProducts().isEmpty()) {
			throw new AppException("category cannot be deleted !", HttpStatus.BAD_REQUEST);
		}
		
		if(c.getSubCategories() != null && !c.getSubCategories().isEmpty()) {
			throw new AppException("category cannot be deleted !", HttpStatus.BAD_REQUEST);
		}
		
		if(c.getImageUrl() != null && c.getPublicUrl() != null) {
			cservice.deleteImage(c.getPublicUrl());
		}
		crepo.deleteById(categoryId);

	}

	@Override
	public List<CategoryDto> getSubCategories(String parentCategoryName) {
		// TODO Auto-generated method stub
		Category parent = crepo.findByCategoryName(parentCategoryName).orElseThrow(() -> new AppException("no parent category found!", HttpStatus.BAD_REQUEST));
		return crepo.findByParentCategoryCategoryId(parent.getCategoryId()).stream().map((c)->{
			CategoryDto dto = mapper.map(c,CategoryDto.class);
			dto.setParentCategoryId(parent.getCategoryId());
			dto.setParentCategoryName(parentCategoryName);
			return dto;
		}).collect(Collectors.toList());
	}

}
