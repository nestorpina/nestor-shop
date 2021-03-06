package com.igz.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.NamespaceManager;
import com.googlecode.objectify.ObjectifyService;
import com.igz.entity.category.CategoryDto;
import com.igz.entity.category.CategoryManager;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;
import com.igz.exception.IgzException;
import com.igzcode.java.util.Trace;

public class TestHelper {
	
	private static final Logger LOGGER = Logger.getLogger(TestHelper.class.getName());

	public static CategoryDto category1;
	public static CategoryDto category2;
	public static ProductDto product1;
	public static ProductDto product2;
	public static ProductDto product3;
	public static List<ProductDto> products;
	public static UserDto user;
	

	public static void prepareDS(){
		NamespaceManager.set("test");
		
        ObjectifyService.register(UserDto.class);
        ObjectifyService.register(CategoryDto.class);
        ObjectifyService.register(ProductDto.class);
        ObjectifyService.register(ShoppingListDto.class);
        ObjectifyService.register(ShoppingListItemDto.class);

        loadTestData();
	}


	public static void loadTestData() {
		UserManager userM = new UserManager();
		user = new UserDto();

		user.setEmail("nestor.pina@intelygenz.com");
		user.setFullname( "Nestor Pina" );
		userM.save( user );
		
		CategoryManager categoryM = new CategoryManager();
		category1 = new CategoryDto();
		category1.setId(1L);
		category1.setName("Food");
		category1.setCreationDate(new Date());
		categoryM.save(category1);
		
		category2 = new CategoryDto();
		category2.setId(2L);
		category2.setName("Gadgets");
		category2.setCreationDate(new Date());
		categoryM.save(category2);
		
    	products = new ArrayList<ProductDto>();

		ProductManager productM = new ProductManager();
		product1 = new ProductDto();
    	product1.setCreationDate(new Date());
    	product1.setName("Pack of 6 Eggs");
    	product1.setDescription("Fresh eggs");
    	product1.setUnitType(ProductDto.UnitType.ITEM);
    	product1.setUnits(6);
    	product1.setId(1L);
    	product1.setCategory(category1);
    	productM.save(product1);
    	products.add(product1);
   	
		product2 = new ProductDto();
		product2.setCreationDate(new Date());
		product2.setName("Rice");
		product2.setDescription("a pack of rice");
		product2.setUnitType(ProductDto.UnitType.KG);
		product2.setUnits(1);
		product2.setId(2L);
		product2.setCategory(category1);
		productM.save(product2);
    	products.add(product2);
	
		product3 = new ProductDto();
		product3.setCreationDate(new Date());
		product3.setName("Ipad");
		product3.setDescription("latest apple ipad");
		product3.setUnitType(ProductDto.UnitType.ITEM);
		product3.setUnits(1);
		product3.setId(3L);
		product3.setCategory(category2);
		productM.save(product3);		
		products.add(product3);

		ProductDto p = new ProductDto();
		p.setCreationDate(new Date());
		p.setName("Samsung Galaxy S20");
		p.setDescription("Samsung mobile phone with 20 inches screen");
		p.setUnitType(ProductDto.UnitType.ITEM);
		p.setUnits(1);
		p.setId(4L);
		p.setCategory(category2);
		productM.save(p);	
		products.add(p);

		p = new ProductDto();
		p.setCreationDate(new Date());
		p.setName("Brick of Milk");
		p.setDescription("A liter of fresh milk");
		p.setUnitType(ProductDto.UnitType.LITER);
		p.setUnits(1);
		p.setId(5L);
		p.setCategory(category1);
		productM.save(p);	
		products.add(p);

		p = new ProductDto();
		p.setCreationDate(new Date());
		p.setName("Pack of 6 Milk Bricks");
		p.setDescription("6 liters of fresh milk");
		p.setUnitType(ProductDto.UnitType.LITER);
		p.setUnits(6);
		p.setId(6L);
		p.setCategory(category1);
		productM.save(p);	
		products.add(p);

    	
    	ShoppingListManager slM = new ShoppingListManager();
    	ShoppingListDto sl = new ShoppingListDto();
    	sl.setCreationDate(new Date());
    	sl.setName("my shopping list");
    	sl.setOwner(user.getKey());
    	sl.setId(1L);
    	slM.save( sl );
    	
    	try {
    	slM.addProduct(sl.getKey(), product1);
			slM.addProduct(sl.getKey(), product2);
		} catch (IgzException e) {
			LOGGER.severe(Trace.error(e));
		}
    	
	}
	
}
