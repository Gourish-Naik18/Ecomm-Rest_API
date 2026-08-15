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

import com.product.dto.BrandDto;
import com.product.request.AddBrandRequest;
import com.product.request.UpdateBrandRequest;
import com.product.response.ApiResponse;
import com.product.service.BrandService;


@RestController
@RequestMapping("/brand")
public class BrandController {
	
	@Autowired
	private BrandService bservice;
	
	@PostMapping(value="/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addBrand(@RequestParam String brandName,
			       @RequestPart(value="image",required = false) MultipartFile image){
		AddBrandRequest request = new AddBrandRequest();
		request.setBrandName(brandName);
		BrandDto dto = bservice.addBrand(request, image);
		return ResponseEntity.ok(new ApiResponse<>("brand added sucessfully!",dto,HttpStatus.OK));
	}

	@GetMapping("/get/{brandId}")
	public ResponseEntity<?> getBrandById(@PathVariable Integer brandId){
		BrandDto dto = bservice.getById(brandId);
		return ResponseEntity.ok(new ApiResponse<>("brand data!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAllBrands(){
		List<BrandDto> list = bservice.getAllBrands();
		return ResponseEntity.ok(new ApiResponse<>("brand data!",list,HttpStatus.OK));
	}
	
	@PutMapping(value="/update/{brandId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateBrand(@PathVariable Integer brandId,@RequestParam String brandName,@RequestPart(value="image",required = false) MultipartFile image){
		UpdateBrandRequest request = new UpdateBrandRequest();
		request.setBrandName(brandName);
		BrandDto dto = bservice.updateBrand(brandId, request, image);
		return ResponseEntity.ok(new ApiResponse<>("updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{brandId}")
	public ResponseEntity<?> deleteBrand(@PathVariable Integer brandId){
		bservice.deleteBrandById(brandId);
		return ResponseEntity.ok(new ApiResponse<>("deleted sucessfully!",null,HttpStatus.OK));
	}
	
	
	
	
	
	
	
	
}
