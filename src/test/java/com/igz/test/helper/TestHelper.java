package com.igz.test.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.NamespaceManager;
import com.googlecode.objectify.ObjectifyService;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;

public class TestHelper {

	public static ProductDto product1;
	public static ProductDto product2;
	public static List<ProductDto> products = null;
	

	public static void prepareDS(){
		NamespaceManager.set("test");
		
        ObjectifyService.register(UserDto.class);
        ObjectifyService.register(ProductDto.class);
        ObjectifyService.register(ShoppingListDto.class);
        ObjectifyService.register(ShoppingListItemDto.class);

        // Test data
		UserManager userM = new UserManager();
		UserDto user = new UserDto();

		user.setEmail("nestor.pina@intelygenz.com");
		user.setFullname( "Nestor Pina" );
		userM.save( user );
		
		ProductManager productM = new ProductManager();
		product1 = new ProductDto();
    	product1.setCreationDate(new Date());
    	product1.setName("Pack of 6 Eggs");
    	product1.setDescription("Fresh eggs");
    	product1.setUnitType(ProductDto.UnitType.ITEM);
    	product1.setUnits(6);
    	product1.setId(1L);
    	productM.save(product1);
    	
    	product2 = new ProductDto();
    	product2.setCreationDate(new Date());
    	product2.setName("Rice");
    	product2.setDescription("a pack of rice");
    	product2.setUnitType(ProductDto.UnitType.KG);
    	product2.setUnits(1);
    	product2.setId(2L);
    	productM.save(product2);
    	
    	products = new ArrayList<ProductDto>();
    	products.add(product1);
    	products.add(product2);
	}
	
}
