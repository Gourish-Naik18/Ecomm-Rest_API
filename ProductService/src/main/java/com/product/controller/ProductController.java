package com.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.product.dto.ProductDto;
import com.product.request.AddProductRequest;
import com.product.request.UpdateProductRequest;
import com.product.response.ApiResponse;
import com.product.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService pservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@RequestBody AddProductRequest request){
		ProductDto dto = pservice.addProduct(request);
		return ResponseEntity.ok(new ApiResponse<>("product added sucessfully!",dto,HttpStatus.OK));
	}

	@GetMapping("/get/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable Integer productId){
		ProductDto dto = pservice.getProductById(productId);
		return ResponseEntity.ok(new ApiResponse<>("Product Data",dto,HttpStatus.OK));
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllProducts(){
		List<ProductDto> li = pservice.getAllProducts();
		return ResponseEntity.ok(new ApiResponse<>("product data!",li,HttpStatus.OK));
	}
	
	@GetMapping("/getCategory/{categoryName}")
	public ResponseEntity<?> getByCategory(@PathVariable String categoryName){
		List<ProductDto> li = pservice.getProductsByCategory(categoryName);
		return ResponseEntity.ok(new ApiResponse<>("product data",li,HttpStatus.OK));
	}
	
	@GetMapping("/getBrand/{brandName}")
	public ResponseEntity<?> getByBrand(@PathVariable String brandName){
		List<ProductDto> li = pservice.getProductsByBrand(brandName);
		return ResponseEntity.ok(new ApiResponse<>("product data",li,HttpStatus.OK));
	}
	
	@PutMapping("/update/{productId}")
	public ResponseEntity<?> updateProduct(@PathVariable Integer productId,@RequestBody UpdateProductRequest request){
		ProductDto dto = pservice.updateProduct(productId, request);
		return ResponseEntity.ok(new ApiResponse<>("updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
		pservice.deleteProductById(productId);
		return ResponseEntity.ok(new ApiResponse<>("Deleted sucessfully!",null,HttpStatus.OK));
	}
	
	
}
