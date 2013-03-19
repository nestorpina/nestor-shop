package com.igz.entity.product;

import static com.googlecode.objectify.ObjectifyService.ofy;


public class ProductManager extends ProductFactory {
	
	public ProductDto getByLongId(Long id) {
		return ofy().load().type(ProductDto.class).id(id).get();
	}
}
