package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.Category;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
	
	Optional<Category> findByCategoryName(String categoryName);
	
	List<Category> findByParentCategoryIsNull();
	
	List<Category> findByParentCategoryCategoryId(Integer parentCategoryId);

}
