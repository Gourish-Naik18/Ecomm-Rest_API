package com.product.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddProductVariantRequest {
	
	@NotNull(message = "price is required")
	@Positive(message = "price must be greater the zero")
	private Double price;
	
//	@NotNull(message = "stocks is required")
//	@Positive(message = "stocks cannot be negative")
//	private Integer stocks;
	
//	@NotEmpty(message = "atleast one attribute name is required for the variant")
//	private List<String> attributeNames;
	
	@NotEmpty(message = "at least one attribute-value pair is required")
	@Valid
	private List<AttributeHelperRequest> attributes;

}
