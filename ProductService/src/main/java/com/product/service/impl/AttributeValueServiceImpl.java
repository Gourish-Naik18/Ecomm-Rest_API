package com.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dto.AttributeValueDto;
import com.product.dto.ProductAttributeDto;
import com.product.entity.AttributeValue;
import com.product.entity.ProductAttribute;
import com.product.exception.AppException;
import com.product.repo.AttributeValueRepo;
import com.product.repo.ProductAttributeRepo;
import com.product.request.AddAttributeValueRequest;
import com.product.request.UpdateAttributeValueRequest;
import com.product.service.AttributeValueService;

@Service
public class AttributeValueServiceImpl implements AttributeValueService {

	@Autowired
	private AttributeValueRepo arepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductAttributeRepo prepo;

	@Override
	@Transactional
	public AttributeValueDto addAttributeValue(AddAttributeValueRequest request) {
		// TODO Auto-generated method stub
		ProductAttribute attribute = prepo.findByAttributeNameIgnoreCase(request.getAttributeName())
				.orElseThrow(() -> new AppException("no attribute found", HttpStatus.NOT_FOUND));

		AttributeValue value = arepo.findByProductAttributeAttributeNameIgnoreCaseAndValueNameIgnoreCase(
				request.getAttributeName(), request.getValueName()).orElse(null);
		if (value != null) {
			throw new AppException("value attribute pair already exists!", HttpStatus.CONFLICT);
		}

		AttributeValue value1 = new AttributeValue();
		value1.setValueName(request.getValueName());
		value1.setProductAttribute(attribute);

		AttributeValue saved = arepo.save(value1);

		AttributeValueDto dto = mapper.map(saved, AttributeValueDto.class);
		dto.setProductAttribute(mapper.map(attribute, ProductAttributeDto.class));

		return dto;
	}

	@Override
	@Transactional
	public List<AttributeValueDto> getValuesByAttributeId(Integer attributeId) {
		// TODO Auto-generated method stub
		ProductAttribute attribute = prepo.findById(attributeId)
				.orElseThrow(() -> new AppException("no attribute found", HttpStatus.NOT_FOUND));
		return arepo.findByProductAttributeAttributeId(attributeId).stream().map((val) -> {
			AttributeValueDto dto = mapper.map(val, AttributeValueDto.class);
			dto.setProductAttribute(mapper.map(attribute, ProductAttributeDto.class));
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AttributeValueDto getValueById(Integer valueId) {
		// TODO Auto-generated method stub
		AttributeValue value = arepo.findById(valueId)
				.orElseThrow(() -> new AppException("no attributevalue found!", HttpStatus.NOT_FOUND));
		AttributeValueDto dto = mapper.map(value, AttributeValueDto.class);
		dto.setProductAttribute(mapper.map(value.getProductAttribute(), ProductAttributeDto.class));
		return dto;
	}

	@Override
	@Transactional
	public AttributeValueDto updateAttributeValue(Integer valueId, UpdateAttributeValueRequest request) {
		// TODO Auto-generated method stub
		AttributeValue value = arepo.findById(valueId)
				.orElseThrow(() -> new AppException("no attributevalue found!", HttpStatus.NOT_FOUND));

		AttributeValue value1 = arepo.findByProductAttributeAttributeNameIgnoreCaseAndValueNameIgnoreCase(
				value.getProductAttribute().getAttributeName(), request.getValueName()).orElse(null);
		if (value1 != null && !value1.getValueId().equals(valueId)) {
			throw new AppException("value attribute pair already exists!", HttpStatus.CONFLICT);
		}

		value.setValueName(request.getValueName());
		AttributeValue saved = arepo.save(value);

		AttributeValueDto dto = mapper.map(saved, AttributeValueDto.class);
		dto.setProductAttribute(mapper.map(saved.getProductAttribute(), ProductAttributeDto.class));

		return dto;
	}

	@Override
	@Transactional
	public void deleteAttributeValue(Integer valueId) {
		// TODO Auto-generated method stub
		AttributeValue value = arepo.findById(valueId)
				.orElseThrow(() -> new AppException("no attributevalue found!", HttpStatus.NOT_FOUND));

		if(value.getVariantValues() != null && !value.getVariantValues().isEmpty()) {
			throw new AppException("cannot delete attribute value!",HttpStatus.BAD_REQUEST);
		}

		arepo.delete(value);
	}

}
