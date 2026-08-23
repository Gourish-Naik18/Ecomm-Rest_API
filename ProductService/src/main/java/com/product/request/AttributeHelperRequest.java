package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttributeHelperRequest {

	@NotBlank(message = "attribute name is required")
	private String attributeName;
	
	@NotBlank(message = "value name is required")
	private String valueName;
}
