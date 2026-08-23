package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.ProductVariant;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductVariantRepo extends JpaRepository<ProductVariant, Integer> {
	
	Optional<ProductVariant> findBySku(String sku);
	
	List<ProductVariant> findByProductProductId(Integer productId);

}
