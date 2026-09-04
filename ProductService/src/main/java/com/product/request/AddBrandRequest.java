package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBrandRequest {
	
//	@NotBlank(message = "brand name cannot be empty")
	private String brandName;
	
	private String about;
	
	private String status = "ACTIVE";

}
