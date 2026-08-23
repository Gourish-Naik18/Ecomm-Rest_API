package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAttributeValueRequest {
	
	@NotBlank(message = "value name is required")
	private String valueName;

}
