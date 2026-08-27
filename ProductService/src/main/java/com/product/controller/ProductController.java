package com.product.controller;

import java.util.ArrayList;
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

import com.product.dto.ProductDto;
import com.product.exception.AppException;
import com.product.request.AddProductRequest;
import com.product.request.AttributeHelperRequest;
import com.product.request.UpdateProductRequest;
import com.product.response.ApiResponse;
import com.product.service.ProductImageService;
import com.product.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService pservice;
	
	@Autowired
	private ProductImageService piservice;
	
	@PostMapping(value="/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addProduct(@RequestParam String productName , @RequestParam String description,@RequestParam String categoryName,@RequestParam String brandName,@RequestPart(value = "images",required = false) List<MultipartFile> images){
		
//		if(attributeNames == null || valueNames == null || attributeNames.isEmpty() || valueNames.isEmpty()) {
//			throw new AppException("give both attribute names and attribute values",HttpStatus.BAD_REQUEST);
//		}
//		
//		if(attributeNames.size() != valueNames.size()) {
//			throw new AppException("no of attributenames does not match with attribute value", HttpStatus.BAD_REQUEST);
//		}
//		
//		List<AttributeHelperRequest> list = new ArrayList<>();
//		for(int i=0;i<attributeNames.size();i++) {
//			String attr = attributeNames.get(i);
//			String val = valueNames.get(i);
//			if(attr == null || attr.trim().isEmpty() || val == null || val.trim().isEmpty()) {
//				throw new AppException("attribute name and value name must not be empty",HttpStatus.BAD_REQUEST);
//			}
//			
//			AttributeHelperRequest req = new AttributeHelperRequest();
//			req.setAttributeName(attr.trim());
//			req.setValueName(val.trim());
//			list.add(req);
//		}
		
		AddProductRequest request = new AddProductRequest();
		request.setProductName(productName);
//		request.setPrice(price);
//		request.setStocks(stocks);
		request.setDescription(description);
		request.setBrandName(brandName);
		request.setCategoryName(categoryName);
//		request.setAttributes(list);
		
		ProductDto dto = pservice.addProduct(request,images);
		return ResponseEntity.ok(new ApiResponse<>("product added sucessfully!",dto,HttpStatus.OK));
	}
	
//	@PostMapping(value="/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public ResponseEntity<?> addProduct(@RequestPart("product") AddProductRequest request,@RequestPart(value = "images",required = false) List<MultipartFile> images){
//		ProductDto dto = pservice.addProduct(request, images);
//		return ResponseEntity.ok(new ApiResponse<>("product added sucessfully!",dto,HttpStatus.OK));
//	}

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
	
	@PutMapping(value="/update/{productId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateProduct(@PathVariable Integer productId,@RequestParam String productName ,@RequestParam String description,@RequestParam String categoryName,@RequestParam String brandName,@RequestPart(value = "images",required = false) List<MultipartFile> images){
		
		UpdateProductRequest request = new UpdateProductRequest();
		request.setProductName(productName);
		request.setDescription(description);
		request.setBrandName(brandName);
		request.setCategoryName(categoryName);
		
		ProductDto dto = pservice.updateProduct(productId, request, images);
		return ResponseEntity.ok(new ApiResponse<>("updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
		pservice.deleteProductById(productId);
		return ResponseEntity.ok(new ApiResponse<>("Deleted sucessfully!",null,HttpStatus.OK));
	}
	
	
	@DeleteMapping("/deleteImage/{imageId}")
	public ResponseEntity<?> deleteImageById(@PathVariable Integer imageId){
		piservice.deleteImageById(imageId);
		return ResponseEntity.ok(new ApiResponse<>("Image Deleted sucessfully!",null,HttpStatus.OK));
		
	}
	
}
