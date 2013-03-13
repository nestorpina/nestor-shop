package com.igz.entity.category;

import com.igz.java.gae.pattern.AbstractFactoryPlus;

public class CategoryFactory extends AbstractFactoryPlus<CategoryDto> {

	protected CategoryFactory() {
		super(CategoryDto.class);
	}

}
