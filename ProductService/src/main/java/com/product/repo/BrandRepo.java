package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Brand;
import java.util.List;
import java.util.Optional;


@Repository
public interface BrandRepo extends JpaRepository<Brand, Integer> {
	
	Optional<Brand> findByBrandName(String brandName);

}
