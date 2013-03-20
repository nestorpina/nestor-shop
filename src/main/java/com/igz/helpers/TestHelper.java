package com.igz.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class TestHelper {

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
    	
		product2 = new ProductDto();
		product2.setCreationDate(new Date());
		product2.setName("Rice");
		product2.setDescription("a pack of rice");
		product2.setUnitType(ProductDto.UnitType.KG);
		product2.setUnits(1);
		product2.setId(2L);
		product2.setCategory(category1);
		productM.save(product2);
		
		product3 = new ProductDto();
		product3.setCreationDate(new Date());
		product3.setName("Ipad");
		product3.setDescription("latest apple ipad");
		product3.setUnitType(ProductDto.UnitType.ITEM);
		product3.setUnits(1);
		product3.setId(3L);
		product3.setCategory(category2);
		productM.save(product3);		
    	
    	products = new ArrayList<ProductDto>();
    	products.add(product1);
    	products.add(product2);
    	products.add(product3);
    	
    	ShoppingListManager slM = new ShoppingListManager();
    	ShoppingListDto sl = new ShoppingListDto();
    	sl.setCreationDate(new Date());
    	sl.setName("my shopping list");
    	sl.setOwner(user.getKey());
    	sl.setId(1L);
    	slM.save( sl );
    	
    	slM.addProduct(sl, product1);
    	slM.addProduct(sl, product2);
    	
	}
	
}
