package com.product.service;

import java.util.List;

import com.product.dto.AttributeValueDto;
import com.product.request.AddAttributeValueRequest;
import com.product.request.UpdateAttributeValueRequest;

public interface AttributeValueService {
	
	AttributeValueDto addAttributeValue(AddAttributeValueRequest request);
	
	List<AttributeValueDto> getValuesByAttributeId(Integer attributeId);
	
	AttributeValueDto getValueById(Integer valueId);
	
	AttributeValueDto updateAttributeValue(Integer valueId,UpdateAttributeValueRequest request);
	
	void deleteAttributeValue(Integer valueId);

}
