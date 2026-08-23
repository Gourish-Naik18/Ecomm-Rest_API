package com.product.request;

import java.util.List;

import lombok.Data;

@Data
public class AddProductVariantRequest {
	
	private Double price;
	
	private Integer stocks;
	
	private List<AttributeHelperRequest> attributes;

}
