package com.product.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.entity.VariantValue;

public interface VariantValueRepo extends JpaRepository<VariantValue, Integer> {
	
	List<VariantValue> findByProductVariantProductVariantId(Integer productVariantId);

}
