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

import com.product.dto.ProductVariantDto;
import com.product.request.AddProductVariantRequest;
import com.product.request.UpdateProductVariantRequest;
import com.product.response.ApiResponse;
import com.product.service.ProductVariantService;

@RestController
@RequestMapping("/productVariant")
public class ProductVariantController {
	
	@Autowired
	private ProductVariantService pservice;
	
	@PostMapping("/add/{productId}")
	public ResponseEntity<?> addProductVariant(@PathVariable Integer productId,@RequestBody AddProductVariantRequest request){
		ProductVariantDto dto = pservice.addProductVariant(productId, request);
		return ResponseEntity.ok(new ApiResponse<>("variant added sucessfully!",dto,HttpStatus.OK));
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateProductVariant(@RequestBody UpdateProductVariantRequest request){
		ProductVariantDto dto = pservice.updateProductVariant(request);
		return ResponseEntity.ok(new ApiResponse<>("variant updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/getByProduct/{productId}")
	public ResponseEntity<?> getVariantsByProductId(@PathVariable Integer productId){
		List<ProductVariantDto> li = pservice.getVariantsByProductId(productId);
		return ResponseEntity.ok(new ApiResponse<>("variants by product",li,HttpStatus.OK));
	}
	
	@GetMapping("/getBySku/{sku}")
	public ResponseEntity<?> getVariantsBySku(@PathVariable String sku){
		ProductVariantDto dto = pservice.getVariantBySku(sku);
		return ResponseEntity.ok(new ApiResponse<>("variant by sku",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{productVariantId}")
	public ResponseEntity<?> deleteProductVariant(@PathVariable Integer productVariantId){
		pservice.deleteProductVariant(productVariantId);
		return ResponseEntity.ok(new ApiResponse<>("variant deleted sucessfully!",null,HttpStatus.OK));
	}

}
