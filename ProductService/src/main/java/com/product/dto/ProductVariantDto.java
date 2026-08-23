package com.product.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductVariantDto {
	
	private Integer productVariantId;
	
	private String sku;
	
	private Double price;
	
	private Integer stocks;
	
	private List<VariantValueDto> variantValues;

}
