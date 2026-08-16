package com.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateProductRequest {
	
//	@NotBlank(message = "product name cannot be empty")
	private String productName;
	
//	@NotBlank(message = "description cannot be empty")
	private String description;
	
//	@NotBlank(message = "price cannot be empty")
//	@Positive(message = "price must be greater then zero")
	private Double price;
	
//	@NotBlank(message = "category name cannot be empty")
	private String categoryName;
	
//	@NotBlank(message = "brand name cannot be empty")
	private String brandName;

}
