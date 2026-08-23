package com.product.service;

import java.util.List;

import com.product.dto.ProductVariantDto;
import com.product.request.AddProductVariantRequest;
import com.product.request.UpdateProductVariantRequest;

public interface ProductVariantService {
	
	ProductVariantDto addProductVariant(Integer productId,AddProductVariantRequest request);
	
	ProductVariantDto updateProductVariant(UpdateProductVariantRequest request);  //instead of id -- sku
    
	List<ProductVariantDto> getVariantsByProductId(Integer productId);
	
	ProductVariantDto getVariantBySku(String sku);
	
	void deleteProductVariant(Integer productVariantId);
}
