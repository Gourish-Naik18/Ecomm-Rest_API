package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.exception.AppException;
import com.product.dto.ProductAttributeDto;
import com.product.request.AddProductAttributeRequest;
import com.product.request.UpdateProductAttributeRequest;
import com.product.response.ApiResponse;
import com.product.service.ProductAttributeService;


@RestController
@RequestMapping("/productAttribute")
public class ProductAttributeController {
	
	@Autowired
	private ProductAttributeService pservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> addAttribute(@Validated @RequestBody AddProductAttributeRequest request,BindingResult result){
		
		if(result.hasErrors()) {
			throw new AppException(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ProductAttributeDto dto = pservice.addAttribute(request);
		return ResponseEntity.ok(new ApiResponse<>("attribute added sucessfully!",dto,HttpStatus.OK));
	}
	
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllAttributes(){
		List<ProductAttributeDto> li = pservice.getAllAttributes();
		return ResponseEntity.ok(new ApiResponse<>("attributes",li,HttpStatus.OK));
	}
	
	
	@GetMapping("/get/{attributeId}")
	public ResponseEntity<?> getAttributeById(@PathVariable Integer attributeId){
		ProductAttributeDto dto = pservice.getAttributeById(attributeId);
		return ResponseEntity.ok(new ApiResponse<>("attribute",dto,HttpStatus.OK));
	}
	
	@PutMapping("/update/{attributeId}")
	public ResponseEntity<?> updateAttribute(@PathVariable Integer attributeId ,@Validated @RequestBody UpdateProductAttributeRequest request,BindingResult result){
		
		if(result.hasErrors()) {
			throw new AppException(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ProductAttributeDto dto = pservice.updateAttribute(attributeId, request);
		return ResponseEntity.ok(new ApiResponse<>("attribute updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{attributeId}")
	public ResponseEntity<?> deleteAttribute(@PathVariable Integer attributeId){
		pservice.deleteAttribute(attributeId);
		return ResponseEntity.ok(new ApiResponse<>("attribute deleted sucessfully!",null,HttpStatus.OK));
	}
	
}
