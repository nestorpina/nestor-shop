package com.igz.entity.category;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.igz.exception.IgzException;


public class CategoryManager extends CategoryFactory {
	
	public CategoryDto getByLongId(Long id) throws IgzException {
		CategoryDto CategoryDto = ofy().load().type(CategoryDto.class).id(id).get();
		if(CategoryDto == null) {
			throw new IgzException(IgzException.IGZ_INVALID_CATEGORY);
		}
		return CategoryDto;
	}
}
