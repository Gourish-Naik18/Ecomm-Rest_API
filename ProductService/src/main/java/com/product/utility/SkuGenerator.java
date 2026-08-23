package com.product.utility;

public class SkuGenerator {
	
	public static String generateSku(String brandName,String categoryName) {
		
		String random = "d3b07384d11346fba0448da05615d04556rgourxdferqwrlpkohvmdlt63s8";
		
		String code = "";
		
		for(int i=0;i<6;i++) {
			int index = (int) (Math.random()*random.length());
			code += random.charAt(index);
		}
		
		String sku="";
		
		if(brandName.length() >= 3) {
			sku = brandName.substring(0, 3).toUpperCase()+"-"+categoryName.substring(0, 3).toUpperCase()+"-"+code.toUpperCase();
		}
		else {
			sku = brandName.substring(0, 2).toUpperCase()+"-"+categoryName.substring(0, 3).toUpperCase()+"-"+code.toUpperCase();
		}
		
		return sku;
	}

}
