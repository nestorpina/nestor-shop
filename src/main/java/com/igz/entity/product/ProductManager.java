package com.igz.entity.product;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.igz.exception.IgzException;


public class ProductManager extends ProductFactory {
	
	public ProductDto getByLongId(Long id) throws IgzException {
		ProductDto productDto = ofy().load().type(ProductDto.class).id(id).get();
		if(productDto == null) {
			throw new IgzException(IgzException.IGZ_INVALID_PRODUCT);
		}
		return productDto;
	}
}
