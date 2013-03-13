package com.igz.entity.shoppinglistitem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.igz.entity.product.ProductDto;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igzcode.java.gae.pattern.AbstractFactory;

public class ShoppingListItemFactory extends
		AbstractFactory<ShoppingListItemDto> {

	public ShoppingListItemFactory() {
		super(ShoppingListItemDto.class);
	}
	
	public ShoppingListItemDto put(Key<ShoppingListDto> shoppingListKey,
			ProductDto product, int quantity) {
		ShoppingListItemDto item = new ShoppingListItemDto();
		item.setDateAdded(new Date());
		item.setProduct(product);
		item.setShoppingListKey(shoppingListKey);
		item.setQuantity(quantity);
		save( item );
		return item;
	}
	
	public ShoppingListItemDto createOrder(Key<ShoppingListDto> shoppingListKey,
			ProductDto product, int quantity) {
		// Check if the item is already on the order list
		List<ShoppingListItemDto> items = ofy().load().type(ShoppingListItemDto.class).ancestor(shoppingListKey).list();
		for (ShoppingListItemDto item : items) {
			// TODO : Cambiarlo a query
			if(item.getProduct().getKey().equals(product.getKey())) {
				item.setQuantity(item.getQuantity() + quantity);
				save (item);
				return item;
			}
		}
		return put(shoppingListKey, product, quantity);
	}

}
