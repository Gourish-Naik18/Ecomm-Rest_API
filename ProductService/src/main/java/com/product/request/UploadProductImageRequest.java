package com.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UploadProductImageRequest {
	
//	@NotBlank(message = "productId cannot be empty")
	private Integer productId;
	
}
