package com.igz.entity.product;

import com.igzcode.java.gae.pattern.AbstractFactory;

public class ProductFactory extends AbstractFactory<ProductDto> {

	protected ProductFactory() {
		super(ProductDto.class);
	}

}
