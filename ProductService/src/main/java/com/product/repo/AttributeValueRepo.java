package com.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.product.entity.AttributeValue;
import java.util.List;
import java.util.Optional;


@Repository
public interface AttributeValueRepo extends JpaRepository<AttributeValue, Integer> {
	
	List<AttributeValue> findByProductAttributeAttributeId(Integer attributeId);
	
	Optional<AttributeValue> findByProductAttributeAttributeNameIgnoreCaseAndValueNameIgnoreCase(String attributeName,String valueName);

}
