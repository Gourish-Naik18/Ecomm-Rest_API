package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.ProductAttribute;
import java.util.List;
import java.util.Optional;


@Repository
public interface ProductAttributeRepo extends JpaRepository<ProductAttribute, Integer> {
	
	Optional<ProductAttribute> findByAttributeNameIgnoreCase(String attributeName);

}
