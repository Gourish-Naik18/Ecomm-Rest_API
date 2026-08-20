package com.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.product.dto.ProductImageDto;
import com.product.entity.Product;
import com.product.entity.ProductImage;
import com.product.exception.AppException;
import com.product.repo.ProductImageRepo;
import com.product.repo.ProductRepo;
import com.product.response.CloudinaryResponse;
import com.product.service.CloudinaryService;
import com.product.service.ProductImageService;

@Service
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	private ProductImageRepo irepo;

	@Autowired
	private ProductRepo prepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CloudinaryService cservice;

	@Override
	public List<ProductImageDto> uploadImages(Integer productId, List<MultipartFile> images) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));

		if (images == null || images.isEmpty()) {
			throw new AppException("please provide images to upload!", HttpStatus.BAD_REQUEST);
		}

		List<ProductImage> list = new ArrayList<>();

		for (MultipartFile img : images) {
			CloudinaryResponse response = cservice.uploadImage(img);
			ProductImage im = new ProductImage();
			im.setImageUrl(response.getImageUrl());
			im.setPublicUrl(response.getPublicId());
			im.setProduct(p);
			list.add(im);
		}

		List<ProductImage> saved = irepo.saveAll(list);

		return saved.stream().map((img) -> mapper.map(img, ProductImageDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<ProductImageDto> getImagesByProductId(Integer productId) {
		// TODO Auto-generated method stub
		Product p = prepo.findById(productId)
				.orElseThrow(() -> new AppException("no product found!", HttpStatus.NOT_FOUND));

		List<ProductImage> list = irepo.findByProductProductId(productId);

		return list.stream().map((img) -> mapper.map(img, ProductImageDto.class)).collect(Collectors.toList());

	}

	@Override
	public void deleteImageById(Integer imageId) {
		// TODO Auto-generated method stub

		ProductImage img = irepo.findById(imageId)
				.orElseThrow(() -> new AppException("no image found", HttpStatus.NOT_FOUND));

		if (img.getImageUrl() != null && img.getPublicUrl() != null) {
			cservice.deleteImage(img.getPublicUrl());
		}

		irepo.deleteById(imageId);
	}

	@Override
	public List<ProductImageDto> getAllImages() {
		// TODO Auto-generated method stub
		List<ProductImage> list = irepo.findAll();

		return list.stream().map((img) -> mapper.map(img, ProductImageDto.class)).collect(Collectors.toList());

	}

}
