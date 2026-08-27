package com.product.dto;

import lombok.Data;

@Data
public class CategoryDto {
	
	private Integer categoryId;
	
	private String categoryName;
	
	private String description;
	
	private String imageUrl;
	
	private String publicUrl;
	
	private Integer parentCategoryId;
	
	private String parentCategoryName;

}
