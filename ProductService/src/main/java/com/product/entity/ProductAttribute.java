package com.product.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer attributeId;
	
	private String attributeName;
	
	@OneToMany(mappedBy = "productAttribute",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<AttributeValue> attributeValues;
	
	@OneToMany(mappedBy = "productAttribute",cascade = CascadeType.ALL)
	private List<VariantAttribute> variantAttributes;
	

}
