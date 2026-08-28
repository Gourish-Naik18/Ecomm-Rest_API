package com.product.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.VariantAttribute;

@Repository
public interface VariantAttributeRepo extends JpaRepository<VariantAttribute, Integer> {

	List<VariantAttribute> findByProductVariantProductVariantId(Integer productVariantId);
	
	//get attributes by product id
	//i have get varaiants by product id
}
