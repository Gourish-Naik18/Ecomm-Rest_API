package com.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dto.BrandDto;
import com.product.dto.CategoryDto;
import com.product.dto.ProductDto;
import com.product.entity.Brand;
import com.product.entity.Category;
import com.product.entity.Product;
import com.product.entity.ProductImage;
import com.product.exception.AppException;
import com.product.repo.BrandRepo;
import com.product.repo.CategoryRepo;
import com.product.repo.ProductRepo;
import com.product.request.AddProductRequest;
import com.product.request.UpdateProductRequest;
import com.product.service.CloudinaryService;
import com.product.service.ProductService;

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

	@Override
	public ProductDto addProduct(AddProductRequest request) {
		// TODO Auto-generated method stub
		Brand b = brepo.findByBrandName(request.getBrandName()).orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
		Category c = crepo.findByCategoryName(request.getCategoryName()).orElseThrow(() -> new AppException("no Category found!", HttpStatus.NOT_FOUND));
        
		Product p = mapper.map(request,Product.class);
		p.setBrand(b);
		p.setCategory(c);
		
		Product saved = prepo.save(p);
		ProductDto dto = mapper.map(saved,ProductDto.class);
		dto.setBrandDto(mapper.map(b,BrandDto.class));
		dto.setCategoryDto(mapper.map(c,CategoryDto.class));
		return dto;
	}

	@Override
	public ProductDto getProductById(Integer productId) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId).orElseThrow(() -> new AppException("no product found!",HttpStatus.NOT_FOUND));
		ProductDto dto = mapper.map(p, ProductDto.class);
		dto.setBrandDto(mapper.map(p.getBrand(),BrandDto.class));
		dto.setCategoryDto(mapper.map(p.getCategory(),CategoryDto.class));
		return dto;
	}

	@Override
	public List<ProductDto> getAllProducts() {
		// TODO Auto-generated method stub
		List<Product> list = prepo.findAll();
		
//		Function<Product, ProductDto> mapToProductDto = product -> {
//		    ProductDto dto = mapper.map(product, ProductDto.class);
//		    dto.setBrandDto(mapper.map(product.getBrand(), BrandDto.class));
//		    dto.setCategoryDto(mapper.map(product.getCategory(), CategoryDto.class));
//		    return dto;
//		};
		
		return list.stream().map((p)->{
			ProductDto dto = mapper.map(p, ProductDto.class);
			dto.setBrandDto(mapper.map(p.getBrand(),BrandDto.class));
			dto.setCategoryDto(mapper.map(p.getCategory(),CategoryDto.class));
			return dto;
		}).collect(Collectors.toList());
	}

	
	
	@Override
	public List<ProductDto> getProductsByCategory(String categoryName) {
		// TODO Auto-generated method stub
		Category c = crepo.findByCategoryName(categoryName).orElseThrow(() -> new AppException("no Category found!", HttpStatus.NOT_FOUND));
        
		List<Product> list = prepo.findByCategoryCategoryId(c.getCategoryId());
		
		return list.stream().map((p)->{
			ProductDto dto = mapper.map(p, ProductDto.class);
			dto.setBrandDto(mapper.map(p.getBrand(),BrandDto.class));
			dto.setCategoryDto(mapper.map(p.getCategory(),CategoryDto.class));
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public List<ProductDto> getProductsByBrand(String brandName) {
		// TODO Auto-generated method stub
		Brand b = brepo.findByBrandName(brandName).orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
		
		List<Product> list = prepo.findByBrandBrandId(b.getBrandId());
		
		return list.stream().map((p)->{
			ProductDto dto = mapper.map(p, ProductDto.class);
			dto.setBrandDto(mapper.map(p.getBrand(),BrandDto.class));
			dto.setCategoryDto(mapper.map(p.getCategory(),CategoryDto.class));
			return dto;
		}).collect(Collectors.toList());
	}

	
	@Override
	public ProductDto updateProduct(Integer productId, UpdateProductRequest request) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId).orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));
		Brand b = brepo.findByBrandName(request.getBrandName()).orElseThrow(() -> new AppException("no brand found!", HttpStatus.NOT_FOUND));
		Category c = crepo.findByCategoryName(request.getCategoryName()).orElseThrow(() -> new AppException("no Category found!", HttpStatus.NOT_FOUND));
        
		mapper.map(request,p);
		p.setBrand(b);
		p.setCategory(c);
		
		Product saved = prepo.save(p);
		
		ProductDto dto = mapper.map(saved,ProductDto.class);
		dto.setBrandDto(mapper.map(b,BrandDto.class));
		dto.setCategoryDto(mapper.map(c, CategoryDto.class));
		
		return dto;
	}

	@Override
	@Transactional
	public void deleteProductById(Integer productId) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId).orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));
        
		if(p.getProductImages() != null) {
			for(ProductImage img : p.getProductImages()) {
				if(img.getImageUrl() != null && img.getPublicUrl() != null) {
					cservice.deleteImage(img.getPublicUrl());
				}
			}
		}
	
		prepo.deleteById(productId);
	}
	
	
}
