package com.product.dto;

import lombok.Data;

@Data
public class AttributeValueDto {
	
	private Integer valueId;
	
	private String valueName;
	
	private ProductAttributeDto productAttribute;

}
