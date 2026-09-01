package com.product.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dto.AttributeValueDto;
import com.product.dto.ProductAttributeDto;
import com.product.dto.ProductVariantDto;
import com.product.dto.VariantValueDto;
import com.product.entity.AttributeValue;
import com.product.entity.Product;
import com.product.entity.ProductAttribute;
import com.product.entity.ProductVariant;
import com.product.entity.VariantValue;
import com.product.exception.AppException;
import com.product.repo.AttributeValueRepo;
import com.product.repo.ProductAttributeRepo;
import com.product.repo.ProductRepo;
import com.product.repo.ProductVariantRepo;
import com.product.request.AddProductVariantRequest;
import com.product.request.AttributeHelperRequest;
import com.product.request.UpdateProductVariantRequest;
import com.product.service.ProductVariantService;
import com.product.utility.SkuGenerator;

@Service
public class ProductVariantServiceImpl implements ProductVariantService {

	@Autowired
	private ProductVariantRepo pvrepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private ProductAttributeRepo parepo;
	
	@Autowired
	private AttributeValueRepo avrepo;

	@Autowired
	private ProductRepo prepo;

	@Override
	@Transactional
	public ProductVariantDto addProductVariant(Integer productId, AddProductVariantRequest request) {
		// TODO Auto-generated method stub
		Product product = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found", HttpStatus.NOT_FOUND));

		if (request.getPrice() == null || request.getPrice() < 0) {
			throw new AppException("price must be non negative", HttpStatus.BAD_REQUEST);
		}

//		if(request.getStocks() == null || request.getStocks() < 0) {
//			throw new AppException("stocks must be non negative", HttpStatus.BAD_REQUEST);
//		}

//		if(request.getAttributes() == null || request.getAttributes().size() < 2) {
//			throw new AppException("there must be atleast 2 variants!", HttpStatus.BAD_REQUEST);
//		}

		// request pairs

//		List<ProductAttribute> reqAttributes = new ArrayList<>();
		List<AttributeValue> reqValues = new ArrayList<>();
		Set<Integer> reqValueIds = new HashSet<>();

		if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
			for (AttributeHelperRequest pair : request.getAttributes()) {
				AttributeValue val = avrepo.findByProductAttributeAttributeNameIgnoreCaseAndValueNameIgnoreCase(pair.getAttributeName().trim(), pair.getValueName().trim()).orElseThrow(() -> new AppException("no attribute value pair found!", HttpStatus.BAD_REQUEST));

				reqValues.add(val);
				reqValueIds.add(val.getValueId());
			}
		}

		// confirm with already existing product variants

		if (!reqValueIds.isEmpty() && product.getVariants() != null) {
			for (ProductVariant existing : product.getVariants()) {
				if (existing.getVariantValues() != null) {
					Set<Integer> existValueIds = existing.getVariantValues().stream()
							.map(vv -> vv.getAttributeValue().getValueId())
							.collect(Collectors.toSet());

					if (existValueIds.equals(reqValueIds)) {
						throw new AppException("variant already exists with this combination of values!", HttpStatus.BAD_REQUEST);
					}
				}
			}
		}

		ProductVariant variant = new ProductVariant();
		variant.setPrice(request.getPrice());
//		variant.setStocks(request.getStocks());
		variant.setProduct(product);
		variant.setSku(
				SkuGenerator.generateSku(product.getBrand().getBrandName(), product.getCategory().getCategoryName()));

		List<VariantValue> variantValues = new ArrayList<>();
		for (AttributeValue val : reqValues) {
			VariantValue vv = new VariantValue();
			vv.setProductVariant(variant);
			vv.setAttributeValue(val);
			variantValues.add(vv);
		}

		variant.setVariantValues(variantValues);

		ProductVariant saved = pvrepo.save(variant);
		ProductVariantDto dto = mapper.map(saved, ProductVariantDto.class);

		if (saved.getVariantValues() != null) {
			List<VariantValueDto> valueDtos = saved.getVariantValues().stream().map(vv -> {
				VariantValueDto vvDto = new VariantValueDto();
				vvDto.setVariantValueId(vv.getVariantValueId());

				AttributeValueDto avDto = mapper.map(vv.getAttributeValue(), AttributeValueDto.class);
				if (vv.getAttributeValue().getProductAttribute() != null) {
					avDto.setProductAttribute(mapper.map(vv.getAttributeValue().getProductAttribute(), ProductAttributeDto.class));
				}
				vvDto.setAttributeValue(avDto);
				return vvDto;
			}).collect(Collectors.toList());

			dto.setVariantValues(valueDtos);
		}

		return dto;
	}

	@Override
	@Transactional
	public ProductVariantDto updateProductVariant(UpdateProductVariantRequest request) {
		// TODO Auto-generated method stub
		if (request.getSku() == null || request.getSku().trim().isEmpty()) {
			throw new AppException("sku is needed to update!", HttpStatus.BAD_REQUEST);
		}

		ProductVariant variant = pvrepo.findBySku(request.getSku().trim())
				.orElseThrow(() -> new AppException("no variant found with sku!!", HttpStatus.NOT_FOUND));

		if (request.getPrice() == null || request.getPrice() < 0) {
			throw new AppException("price must be non negative", HttpStatus.BAD_REQUEST);
		}

		variant.setPrice(request.getPrice());

//		if(request.getStocks() == null || request.getStocks() < 0) {
//			throw new AppException("stocks must be non negative", HttpStatus.BAD_REQUEST);
//		}

//		variant.setStocks(request.getStocks());

		ProductVariant updated = pvrepo.save(variant);
		ProductVariantDto dto = mapper.map(updated, ProductVariantDto.class);

		if (updated.getVariantValues() != null) {
			List<VariantValueDto> valueDtos = updated.getVariantValues().stream().map(vv -> {
				VariantValueDto vvDto = new VariantValueDto();
				vvDto.setVariantValueId(vv.getVariantValueId());

				AttributeValueDto avDto = mapper.map(vv.getAttributeValue(), AttributeValueDto.class);
				if (vv.getAttributeValue().getProductAttribute() != null) {
					avDto.setProductAttribute(mapper.map(vv.getAttributeValue().getProductAttribute(), ProductAttributeDto.class));
				}
				vvDto.setAttributeValue(avDto);
				return vvDto;
			}).collect(Collectors.toList());

			dto.setVariantValues(valueDtos);
		}

		return dto;
	}

	@Override
	@Transactional
	public List<ProductVariantDto> getVariantsByProductId(Integer productId) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));

		return pvrepo.findByProductProductId(productId).stream().map((v) -> {
			ProductVariantDto dto = mapper.map(v, ProductVariantDto.class);

			if (v.getVariantValues() != null) {
				List<VariantValueDto> valueDtos = v.getVariantValues().stream().map(vv -> {
					VariantValueDto vvDto = new VariantValueDto();
					vvDto.setVariantValueId(vv.getVariantValueId());

					AttributeValueDto avDto = mapper.map(vv.getAttributeValue(), AttributeValueDto.class);
					if (vv.getAttributeValue().getProductAttribute() != null) {
						avDto.setProductAttribute(mapper.map(vv.getAttributeValue().getProductAttribute(), ProductAttributeDto.class));
					}
					vvDto.setAttributeValue(avDto);
					return vvDto;
				}).collect(Collectors.toList());

				dto.setVariantValues(valueDtos);
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ProductVariantDto getVariantBySku(String sku) {
		// TODO Auto-generated method stub
		ProductVariant variant = pvrepo.findBySku(sku.trim())
				.orElseThrow(() -> new AppException("no variant found with sku", HttpStatus.NOT_FOUND));
		ProductVariantDto dto = mapper.map(variant, ProductVariantDto.class);

		if (variant.getVariantValues() != null) {
			List<VariantValueDto> valueDtos = variant.getVariantValues().stream().map(vv -> {
				VariantValueDto vvDto = new VariantValueDto();
				vvDto.setVariantValueId(vv.getVariantValueId());

				AttributeValueDto avDto = mapper.map(vv.getAttributeValue(), AttributeValueDto.class);
				if (vv.getAttributeValue().getProductAttribute() != null) {
					avDto.setProductAttribute(mapper.map(vv.getAttributeValue().getProductAttribute(), ProductAttributeDto.class));
				}
				vvDto.setAttributeValue(avDto);
				return vvDto;
			}).collect(Collectors.toList());

			dto.setVariantValues(valueDtos);
		}

		return dto;
	}

	@Override
	@Transactional
	public void deleteProductVariant(Integer productVariantId) {
		// TODO Auto-generated method stub
		ProductVariant variant = pvrepo.findById(productVariantId)
				.orElseThrow(() -> new AppException("no variant found!", HttpStatus.NOT_FOUND));

		// after order items entity
		// if (orderItemRepo.existsByProductVariantProductVariantId(productVariantId)) {
		// throw new AppException("Cannot delete variant: linked to existing customer
		// orders", HttpStatus.BAD_REQUEST);
		// }

		pvrepo.delete(variant);
	}

}
