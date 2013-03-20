package com.igz.entity.product;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.igz.entity.category.CategoryDto;
import com.igz.java.gae.pattern.AbstractFactoryPlus;

public class ProductFactory extends AbstractFactoryPlus<ProductDto> {

	protected ProductFactory() {
		super(ProductDto.class);
	}
	
	public List<ProductDto> findByCategory(CategoryDto category) {
		return ofy().load().type(ProductDto.class).filter(ProductDto.CATEGORY, category).list();
	}

}
