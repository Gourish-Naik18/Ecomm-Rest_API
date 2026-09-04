package com.product.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BrandDto {
	
	private Integer brandId;
	
	private String brandName;
	
	private String imageUrl;
	
	private String publicUrl;
	
	private String about;
	
	private String status;
	
	private LocalDateTime createdAt;

}
