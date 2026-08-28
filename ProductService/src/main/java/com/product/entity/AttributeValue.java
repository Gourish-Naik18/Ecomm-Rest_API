package com.product.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class AttributeValue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer valueId;
	
	private String valueName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id")
	private ProductAttribute productAttribute;
	
//	@OneToMany(mappedBy = "attributeValue",cascade = CascadeType.ALL)
//	private List<VariantValue> variantValues;

}
