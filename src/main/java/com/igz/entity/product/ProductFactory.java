package com.igz.entity.product;

import com.igz.java.gae.pattern.AbstractFactoryPlus;

public class ProductFactory extends AbstractFactoryPlus<ProductDto> {

	protected ProductFactory() {
		super(ProductDto.class);
	}

}
