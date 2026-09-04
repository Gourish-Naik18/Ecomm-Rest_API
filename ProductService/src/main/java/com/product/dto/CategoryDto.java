package com.product.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryDto {
	
	private Integer categoryId;
	
	private String categoryName;
	
	private String description;
	
	private String imageUrl;
	
	private String publicUrl;
	
	private String status;
	
	private LocalDateTime createdAt;
	
	private Integer parentCategoryId;
	
	private String parentCategoryName;

}
