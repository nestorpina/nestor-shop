package com.igz.test.helper;

import java.util.Date;

import com.google.appengine.api.NamespaceManager;
import com.googlecode.objectify.ObjectifyService;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;

public class TestHelper {

	
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
		ProductDto p = new ProductDto();
    	p.setCreationDate(new Date());
    	p.setName("Pack of 6 Eggs");
    	p.setDescription("Fresh eggs");
    	p.setUnitType(ProductDto.UnitType.ITEM);
    	p.setUnits(6);
    	productM.save(p);
    	
    	p = new ProductDto();
    	p.setCreationDate(new Date());
    	p.setName("Rice");
    	p.setDescription("a pack of rice");
    	p.setUnitType(ProductDto.UnitType.KG);
    	p.setUnits(1);
    	productM.save(p);
    	
	}
	
}
