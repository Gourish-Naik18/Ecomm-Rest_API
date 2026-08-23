package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddProductAttributeRequest {
	
	@NotBlank(message = "attribute name is required")
	private String attributeName;

}
