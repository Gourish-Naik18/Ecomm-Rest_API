package com.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dto.ProductAttributeDto;
import com.product.entity.ProductAttribute;
import com.product.exception.AppException;
import com.product.repo.ProductAttributeRepo;
import com.product.request.AddProductAttributeRequest;
import com.product.request.UpdateProductAttributeRequest;
import com.product.service.ProductAttributeService;

@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {

	@Autowired
	private ProductAttributeRepo prepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	@Transactional
	public ProductAttributeDto addAttribute(AddProductAttributeRequest request) {
		// TODO Auto-generated method stub
		ProductAttribute attribute = prepo.findByAttributeNameIgnoreCase(request.getAttributeName()).orElse(null);

		if (attribute != null) {
			throw new AppException("attribute already exists!", HttpStatus.CONFLICT);
		}

		ProductAttribute attribute1 = new ProductAttribute();
		attribute1.setAttributeName(request.getAttributeName());

		ProductAttribute saved = prepo.save(attribute1);

		return mapper.map(saved, ProductAttributeDto.class);
	}

	@Override
	@Transactional
	public List<ProductAttributeDto> getAllAttributes() {
		// TODO Auto-generated method stub
		return prepo.findAll().stream().map((att) -> mapper.map(att, ProductAttributeDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ProductAttributeDto getAttributeById(Integer attributeId) {
		// TODO Auto-generated method stub
		ProductAttribute attribute = prepo.findById(attributeId)
				.orElseThrow(() -> new AppException("no attribute found!", HttpStatus.NOT_FOUND));
		return mapper.map(attribute, ProductAttributeDto.class);
	}

	@Override
	@Transactional
	public ProductAttributeDto updateAttribute(Integer attributeId, UpdateProductAttributeRequest request) {
		// TODO Auto-generated method stub
		ProductAttribute attribute = prepo.findById(attributeId)
				.orElseThrow(() -> new AppException("no attribute found!", HttpStatus.NOT_FOUND));

		ProductAttribute attribute1 = prepo.findByAttributeNameIgnoreCase(request.getAttributeName()).orElse(null);

		if (attribute1 != null && !attribute1.getAttributeId().equals(attributeId)) {
			throw new AppException("attribute already exists!", HttpStatus.CONFLICT);
		}

		attribute.setAttributeName(request.getAttributeName());
		ProductAttribute saved = prepo.save(attribute);

		return mapper.map(saved, ProductAttributeDto.class);
	}

	@Override
	@Transactional
	public void deleteAttribute(Integer attributeId) {
		// TODO Auto-generated method stub
		ProductAttribute attribute = prepo.findById(attributeId)
				.orElseThrow(() -> new AppException("no attribute found!", HttpStatus.NOT_FOUND));

		if ((attribute.getAttributeValues() != null && !attribute.getAttributeValues().isEmpty())
				|| (attribute.getVariantAttributes() != null && !attribute.getVariantAttributes().isEmpty())) {
			throw new AppException("cannot delete attribute", HttpStatus.BAD_REQUEST);
		}

		prepo.delete(attribute);

	}

}
