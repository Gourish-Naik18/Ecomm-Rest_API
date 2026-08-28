package com.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateProductVariantRequest {
	
	@NotBlank(message = "sku is required")
	private String sku;
	
	@Positive(message = "price must be greater the zero")
	private Double price;
	
//	@Positive(message = "stocks cannot be negative")
//	private Integer stocks;

}
