package com.product.request;

import lombok.Data;

@Data
public class UpdateProductVariantRequest {
	
	private String sku;
	
	private Double price;
	
	private Integer stocks;

}
