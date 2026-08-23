package com.product.service;

import java.util.List;

import com.product.dto.ProductAttributeDto;
import com.product.request.AddProductAttributeRequest;
import com.product.request.UpdateProductAttributeRequest;

public interface ProductAttributeService {
	
	
	public ProductAttributeDto addAttribute(AddProductAttributeRequest request);
	
	List<ProductAttributeDto> getAllAttributes();
	
	ProductAttributeDto getAttributeById(Integer attributeId);
	
	ProductAttributeDto updateAttribute(Integer attributeId,UpdateProductAttributeRequest request);
	
	void deleteAttribute(Integer attributeId);

}
