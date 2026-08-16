package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBrandRequest {
	
//	@NotBlank(message = "brand name cannot be empty")
	private String brandName;

}
