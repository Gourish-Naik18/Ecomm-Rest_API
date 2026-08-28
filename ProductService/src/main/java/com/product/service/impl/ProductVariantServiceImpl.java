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

import com.product.dto.ProductAttributeDto;
import com.product.dto.ProductVariantDto;
import com.product.dto.VariantAttributeDto;
import com.product.entity.Product;
import com.product.entity.ProductAttribute;
import com.product.entity.ProductVariant;
import com.product.entity.VariantAttribute;
import com.product.exception.AppException;
import com.product.repo.ProductAttributeRepo;
import com.product.repo.ProductRepo;
import com.product.repo.ProductVariantRepo;
import com.product.request.AddProductVariantRequest;
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

		List<ProductAttribute> reqAttributes = new ArrayList<>();
		Set<Integer> reqId = new HashSet<>();

		if (request.getAttributeNames() != null && !request.getAttributeNames().isEmpty()) {
			for (String attrName : request.getAttributeNames()) {
				ProductAttribute pa = parepo.findByAttributeNameIgnoreCase(attrName.trim())
						.orElseThrow(() -> new AppException("no attribute found!", HttpStatus.NOT_FOUND));

				reqAttributes.add(pa);
				reqId.add(pa.getAttributeId());
			}
		}

		// confirm with already existing product variants

		if (!reqId.isEmpty() && product.getVariants() != null) {
			for (ProductVariant existing : product.getVariants()) {
				if (existing.getVariantAttributes() != null) {
					Set<Integer> existId = existing.getVariantAttributes().stream()
							.map((va) -> va.getProductAttribute().getAttributeId()).collect(Collectors.toSet());

					if (existId.equals(reqId)) {
						throw new AppException("already variant exist for this product!", HttpStatus.BAD_REQUEST);
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

		List<VariantAttribute> values = new ArrayList<>();
		for (ProductAttribute v : reqAttributes) {

			VariantAttribute vv = new VariantAttribute();
			vv.setProductVariant(variant);
			vv.setProductAttribute(v);
			values.add(vv);
		}

		variant.setVariantAttributes(values);

		ProductVariant saved = pvrepo.save(variant);
		ProductVariantDto dto = mapper.map(saved, ProductVariantDto.class);

		if (saved.getVariantAttributes() != null) {
			List<VariantAttributeDto> valueDto = saved.getVariantAttributes().stream().map((vv) -> {
				VariantAttributeDto dt = new VariantAttributeDto();
				dt.setVariantAttributeId(vv.getVariantAttributeId());

				dt.setProductAttribute(mapper.map(vv.getProductAttribute(), ProductAttributeDto.class));
				return dt;
			}).collect(Collectors.toList());

			dto.setVariantAttributes(valueDto);
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

		if (updated.getVariantAttributes() != null) {
			List<VariantAttributeDto> valueDto = updated.getVariantAttributes().stream().map((vv) -> {
				VariantAttributeDto dt = new VariantAttributeDto();
				dt.setVariantAttributeId(vv.getVariantAttributeId());

				dt.setProductAttribute(mapper.map(vv.getProductAttribute(), ProductAttributeDto.class));
				return dt;
			}).collect(Collectors.toList());

			dto.setVariantAttributes(valueDto);
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

			if (v.getVariantAttributes() != null) {
				List<VariantAttributeDto> valueDto = v.getVariantAttributes().stream().map((vv) -> {
					VariantAttributeDto dt = new VariantAttributeDto();
					dt.setVariantAttributeId(vv.getVariantAttributeId());

					dt.setProductAttribute(mapper.map(vv.getProductAttribute(), ProductAttributeDto.class));
					return dt;
				}).collect(Collectors.toList());

				dto.setVariantAttributes(valueDto);
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

		if (variant.getVariantAttributes() != null) {
			List<VariantAttributeDto> valueDto = variant.getVariantAttributes().stream().map((vv) -> {
				VariantAttributeDto dt = new VariantAttributeDto();
				dt.setVariantAttributeId(vv.getVariantAttributeId());

				dt.setProductAttribute(mapper.map(vv.getProductAttribute(), ProductAttributeDto.class));
				return dt;
			}).collect(Collectors.toList());

			dto.setVariantAttributes(valueDto);
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
