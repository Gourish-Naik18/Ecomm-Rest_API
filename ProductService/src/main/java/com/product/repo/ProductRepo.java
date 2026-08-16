package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Product;
import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	
	List<Product> findByProductName(String productName);
	
	List<Product> findByBrandBrandId(Integer brandId);
	
	List<Product> findByCategoryCategoryId(Integer categoryId);

}
