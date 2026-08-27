package com.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.product.dto.CategoryDto;
import com.product.request.AddCategoryRequest;
import com.product.request.UpdateCategoryRequest;

public interface CategoryService {
	
	CategoryDto addCategory(AddCategoryRequest request,MultipartFile image);
	
	CategoryDto getById(Integer categoryId);
	
	List<CategoryDto> getAllCategories();
	
	List<CategoryDto> getSubCategories(String parentCategoryName);
	
	CategoryDto UpdateCategory(Integer categoryId,UpdateCategoryRequest request,MultipartFile image);
	
	void deleteCategoryById(Integer categoryId);

}
