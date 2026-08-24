package com.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.BrandDto;
import com.product.dto.CategoryDto;
import com.product.dto.ProductDto;
import com.product.dto.ProductImageDto;
import com.product.dto.ProductVariantDto;
import com.product.entity.Brand;
import com.product.entity.Category;
import com.product.entity.Product;
import com.product.entity.ProductImage;
import com.product.exception.AppException;
import com.product.repo.BrandRepo;
import com.product.repo.CategoryRepo;
import com.product.repo.ProductRepo;
import com.product.request.AddProductRequest;
import com.product.request.AddProductVariantRequest;
import com.product.request.UpdateProductRequest;
import com.product.service.CloudinaryService;
import com.product.service.ProductImageService;
import com.product.service.ProductService;
import com.product.service.ProductVariantService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo prepo;

	@Autowired
	private BrandRepo brepo;

	@Autowired
	private CategoryRepo crepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CloudinaryService cservice;

	@Autowired
	private ProductImageService pservice;

	@Autowired
	private ProductVariantService pvservice;

	@Override
	@Transactional
	public ProductDto addProduct(AddProductRequest request, List<MultipartFile> images) {
		// TODO Auto-generated method stub
		Brand b = brepo.findByBrandName(request.getBrandName())
				.orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
		Category c = crepo.findByCategoryName(request.getCategoryName())
				.orElseThrow(() -> new AppException("no Category found!", HttpStatus.NOT_FOUND));
		
		if(request.getAttributes() == null || request.getAttributes().isEmpty()) {
			throw new AppException("provide attribute pair for the variant",HttpStatus.BAD_REQUEST);
		}
		
//		if(request.getAttributes() == null || request.getAttributes().size() < 2) {
//			throw new AppException("there must be atleast 2 variants!", HttpStatus.BAD_REQUEST);
//		}

		Product p = new Product();
		p.setProductName(request.getProductName().trim());
		p.setDescription(request.getDescription());
		p.setBrand(b);
		p.setCategory(c);

		Product saved = prepo.save(p);

		List<ProductImageDto> uploaded = new ArrayList<>();
		if (images != null && !images.isEmpty()) {
			uploaded = pservice.uploadImages(saved.getProductId(), images);
		}

		List<ProductVariantDto> variants = new ArrayList<>();
		if (request.getPrice() != null && request.getStocks() != null) {
			AddProductVariantRequest variantRequest = new AddProductVariantRequest();
			variantRequest.setPrice(request.getPrice());
			variantRequest.setStocks(request.getStocks());
			variantRequest.setAttributes(request.getAttributes());

			ProductVariantDto createdVariant = pvservice.addProductVariant(saved.getProductId(), variantRequest);
			variants.add(createdVariant);
		}

		ProductDto dto = mapper.map(saved, ProductDto.class);
		dto.setBrandDto(mapper.map(b, BrandDto.class));
		dto.setCategoryDto(mapper.map(c, CategoryDto.class));
		dto.setProductImageDto(uploaded);
		dto.setVariants(variants);
		return dto;
	}

	@Override
	@Transactional
	public ProductDto getProductById(Integer productId) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));
		ProductDto dto = mapper.map(p, ProductDto.class);
		dto.setBrandDto(mapper.map(p.getBrand(), BrandDto.class));
		dto.setCategoryDto(mapper.map(p.getCategory(), CategoryDto.class));

		if (p.getProductImages() != null) {
			List<ProductImageDto> li = p.getProductImages().stream().map((pi) -> mapper.map(pi, ProductImageDto.class))
					.collect(Collectors.toList());
			dto.setProductImageDto(li);
		} else {
			dto.setProductImageDto(new ArrayList<>());
		}

		dto.setVariants(pvservice.getVariantsByProductId(productId));

		return dto;
	}

	@Override
	@Transactional
	public List<ProductDto> getAllProducts() {
		// TODO Auto-generated method stub
		List<Product> list = prepo.findAll();

//		Function<Product, ProductDto> mapToProductDto = product -> {
//		    ProductDto dto = mapper.map(product, ProductDto.class);
//		    dto.setBrandDto(mapper.map(product.getBrand(), BrandDto.class));
//		    dto.setCategoryDto(mapper.map(product.getCategory(), CategoryDto.class));
//		    return dto;
//		};

		return list.stream().map((p) -> {
			ProductDto dto = mapper.map(p, ProductDto.class);
			dto.setBrandDto(mapper.map(p.getBrand(), BrandDto.class));
			dto.setCategoryDto(mapper.map(p.getCategory(), CategoryDto.class));

			if (p.getProductImages() != null) {
				List<ProductImageDto> li = p.getProductImages().stream()
						.map((pi) -> mapper.map(pi, ProductImageDto.class)).collect(Collectors.toList());
				dto.setProductImageDto(li);
			} else {
				dto.setProductImageDto(new ArrayList<>());
			}

			dto.setVariants(pvservice.getVariantsByProductId(p.getProductId()));

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<ProductDto> getProductsByCategory(String categoryName) {
		// TODO Auto-generated method stub
		Category c = crepo.findByCategoryName(categoryName)
				.orElseThrow(() -> new AppException("no Category found!", HttpStatus.NOT_FOUND));

		List<Product> list = prepo.findByCategoryCategoryId(c.getCategoryId());

		return list.stream().map((p) -> {
			ProductDto dto = mapper.map(p, ProductDto.class);
			dto.setBrandDto(mapper.map(p.getBrand(), BrandDto.class));
			dto.setCategoryDto(mapper.map(p.getCategory(), CategoryDto.class));

			if (p.getProductImages() != null) {
				List<ProductImageDto> li = p.getProductImages().stream()
						.map((pi) -> mapper.map(pi, ProductImageDto.class)).collect(Collectors.toList());
				dto.setProductImageDto(li);
			} else {
				dto.setProductImageDto(new ArrayList<>());
			}

			dto.setVariants(pvservice.getVariantsByProductId(p.getProductId()));

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<ProductDto> getProductsByBrand(String brandName) {
		// TODO Auto-generated method stub
		Brand b = brepo.findByBrandName(brandName)
				.orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));

		List<Product> list = prepo.findByBrandBrandId(b.getBrandId());

		return list.stream().map((p) -> {
			ProductDto dto = mapper.map(p, ProductDto.class);
			dto.setBrandDto(mapper.map(p.getBrand(), BrandDto.class));
			dto.setCategoryDto(mapper.map(p.getCategory(), CategoryDto.class));

			if (p.getProductImages() != null) {
				List<ProductImageDto> li = p.getProductImages().stream()
						.map((pi) -> mapper.map(pi, ProductImageDto.class)).collect(Collectors.toList());
				dto.setProductImageDto(li);
			} else {
				dto.setProductImageDto(new ArrayList<>());
			}

			dto.setVariants(pvservice.getVariantsByProductId(p.getProductId()));

			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ProductDto updateProduct(Integer productId, UpdateProductRequest request, List<MultipartFile> images) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));
		Brand b = brepo.findByBrandName(request.getBrandName())
				.orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
		Category c = crepo.findByCategoryName(request.getCategoryName())
				.orElseThrow(() -> new AppException("no Category found!", HttpStatus.NOT_FOUND));

		p.setProductName(request.getProductName().trim());
		p.setDescription(request.getDescription());
		p.setBrand(b);
		p.setCategory(c);

		Product saved = prepo.save(p);

		if (images != null && !images.isEmpty()) {
			pservice.uploadImages(saved.getProductId(), images);
		}

		ProductDto dto = mapper.map(saved, ProductDto.class);
		dto.setBrandDto(mapper.map(b, BrandDto.class));
		dto.setCategoryDto(mapper.map(c, CategoryDto.class));

		List<ProductImageDto> pdto = pservice.getImagesByProductId(saved.getProductId());
		if (pdto != null) {
			dto.setProductImageDto(pdto);
		} else {
			dto.setProductImageDto(new ArrayList<>());
		}

		dto.setVariants(pvservice.getVariantsByProductId(saved.getProductId()));

		return dto;
	}

	@Override
	@Transactional
	public void deleteProductById(Integer productId) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));

		if (p.getProductImages() != null) {
			for (ProductImage img : p.getProductImages()) {
				if (img.getImageUrl() != null && img.getPublicUrl() != null) {
					cservice.deleteImage(img.getPublicUrl());
				}
			}
		}

		prepo.delete(p);
	}

}
