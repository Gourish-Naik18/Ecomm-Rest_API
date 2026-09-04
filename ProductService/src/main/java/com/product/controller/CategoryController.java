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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.CategoryDto;
import com.product.request.AddCategoryRequest;
import com.product.request.UpdateCategoryRequest;
import com.product.response.ApiResponse;
import com.product.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService cservice;
	
	@PostMapping(value="/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addCategory(@RequestParam String categoryName,
			@RequestParam String description,
			@RequestParam(required = false) String parentCategoryName,
			@RequestParam(value = "status",defaultValue="ACTIVE") String status,
			@RequestPart(value = "image",required = false) MultipartFile image){
		AddCategoryRequest request = new AddCategoryRequest();
		request.setCategoryName(categoryName);
		request.setDescription(description);
		request.setStatus(status);
		request.setParentCategoryName(parentCategoryName);
		CategoryDto dto = cservice.addCategory(request, image);
		return ResponseEntity.ok(new ApiResponse<>("category added sucessfully!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/get/{categoryId}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer categoryId){
		CategoryDto dto = cservice.getById(categoryId);
		return ResponseEntity.ok(new ApiResponse<>("category data!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllCategories(){
		List<CategoryDto> list = cservice.getAllCategories();
		return ResponseEntity.ok(new ApiResponse<>("category data!",list,HttpStatus.OK));
	}
	
	@GetMapping("/getSubCategories/{parentCategoryName}")
	public ResponseEntity<?> getSubCategories(@PathVariable String parentCategoryName){
		List<CategoryDto> li = cservice.getSubCategories(parentCategoryName);
		return ResponseEntity.ok(new ApiResponse<>("sub categories!",li,HttpStatus.OK));
	}
	
	
	@PutMapping(value="/update/{categoryId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId,@RequestParam String categoryName,@RequestParam String description,@RequestParam(required = false) String parentCategoryName,@RequestParam String status,@RequestPart(value="image",required = false) MultipartFile image){
		UpdateCategoryRequest request = new UpdateCategoryRequest();
		request.setCategoryName(categoryName);
		request.setDescription(description);
		request.setParentCategoryName(parentCategoryName);
		request.setStatus(status);
		CategoryDto dto = cservice.UpdateCategory(categoryId, request, image);
		return ResponseEntity.ok(new ApiResponse<>("updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
		cservice.deleteCategoryById(categoryId);
		return ResponseEntity.ok(new ApiResponse<>("deleted sucessfully!",null,HttpStatus.OK));
	}
	

}
