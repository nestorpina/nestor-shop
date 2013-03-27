package com.igz.entity.product;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.igz.entity.category.CategoryDto;
import com.igz.entity.category.CategoryManager;
import com.igz.exception.IgzException;


public class ProductManager extends ProductFactory {
	
	public ProductDto getByLongId(Long id) throws IgzException {
		ProductDto productDto = ofy().load().type(ProductDto.class).id(id).get();
		if(productDto == null) {
			throw new IgzException(IgzException.IGZ_INVALID_PRODUCT);
		}
		return productDto;
	}
	

	/**
	 * Saves a product, checking it's name is unique, and that all the required
	 * data is supplied
	 */
	public ProductDto saveProduct(ProductDto product) throws IgzException {
		// Validate object fields
		if(product.getCategory()==null || product.getCategory().getId()==null ||
				product.getName()==null || product.getUnitType()==null || product.getUnits()==null) {
			throw new IgzException(IgzException.IGZ_INVALID_PARAMS,"Product incomplete, can not save");
		}
		
		// Check if category exists
    	CategoryManager categoryM = new CategoryManager();
    	CategoryDto categoryDto = null;
    	Long categoryId = product.getCategory().getId();
		try {
			categoryDto = categoryM.getByLongId(categoryId);
			product.setCategory(categoryDto);
		} catch (IgzException e) {
			throw new IgzException(IgzException.IGZ_INVALID_CATEGORY);
		}
		
		// Set creation date
    	if(product.getCreationDate() == null) {
    		product.setCreationDate(new Date());
    	}
    	
    	// Check for duplicates
    	List<ProductDto> products = findByProperty(ProductDto.NAME, product.getName());
    	if(products.size() > 0) {
    		throw new IgzException(IgzException.IGZ_DUPLICATE_PRODUCT);
    	}
    	
		save(product);
		
		return product;
	}
}
