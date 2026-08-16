package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.ProductImage;
import java.util.List;


@Repository
public interface ProductImageRepo extends JpaRepository<ProductImage, Integer> {

	List<ProductImage> findByProductProductId(Integer productId);
}
