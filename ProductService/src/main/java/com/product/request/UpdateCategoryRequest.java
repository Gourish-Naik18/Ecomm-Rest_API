package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
	
//	@NotBlank(message = "category name cannot be empty")
	private String categoryName;
	
//	@NotBlank(message = "description cannot be empty")
	private String description;
	
	private String parentCategoryName;

}
