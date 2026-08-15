package com.product.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDto {
	
	private Integer productId;
	
	private String productName;
	
	private String description;
	
	private Double price;
	
	private CategoryDto categoryDto;
	
	private BrandDto brandDto;
	
	private List<ProductImageDto> productImageDto;

}
