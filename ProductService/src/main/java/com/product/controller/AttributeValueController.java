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

import com.product.dto.AttributeValueDto;
import com.product.request.AddAttributeValueRequest;
import com.product.request.UpdateAttributeValueRequest;
import com.product.response.ApiResponse;
import com.product.service.AttributeValueService;

@RestController
@RequestMapping("/attributeValue")
public class AttributeValueController {
	
	@Autowired
	private AttributeValueService aservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> addAttributeValue(@RequestBody AddAttributeValueRequest request){
		AttributeValueDto dto = aservice.addAttributeValue(request);
		return ResponseEntity.ok(new ApiResponse<>("attribute value added sucessfully!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/getByAttribute/{attributeId}")
	public ResponseEntity<?> getValuesByAttributeId(@PathVariable Integer attributeId){
		List<AttributeValueDto> li = aservice.getValuesByAttributeId(attributeId);
		return ResponseEntity.ok(new ApiResponse<>("attribute values",li,HttpStatus.OK));
	}
	
	@GetMapping("/get/{valueId}")
	public ResponseEntity<?> getByValueId(@PathVariable Integer valueId){
		AttributeValueDto dto = aservice.getValueById(valueId);
		return ResponseEntity.ok(new ApiResponse<>("attribute value",dto,HttpStatus.OK));
	}
	
	@PutMapping("/update/{valueId}")
	public ResponseEntity<?> updateAttributeValue(@PathVariable Integer valueId,@RequestBody UpdateAttributeValueRequest request){
		AttributeValueDto dto = aservice.updateAttributeValue(valueId, request);
		return ResponseEntity.ok(new ApiResponse<>("attribute value updated sucessfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{valueId}")
	public ResponseEntity<?> deleteAttributeValue(@PathVariable Integer valueId){
		aservice.deleteAttributeValue(valueId);
		return ResponseEntity.ok(new ApiResponse<>("attribute value deleted sucessfully!",null,HttpStatus.OK));
	}

}
