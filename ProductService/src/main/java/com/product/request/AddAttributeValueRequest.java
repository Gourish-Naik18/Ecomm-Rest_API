package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddAttributeValueRequest {
	
	@NotBlank(message = "attribute name is required")
	private String attributeName;
	
	@NotBlank(message = "value name is required")
	private String valueName;

}
