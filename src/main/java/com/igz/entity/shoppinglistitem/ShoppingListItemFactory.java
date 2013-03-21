package com.igz.entity.shoppinglistitem;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;

import com.googlecode.objectify.Key;
import com.igz.entity.product.ProductDto;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.java.gae.pattern.AbstractFactoryPlus;

public class ShoppingListItemFactory extends
		AbstractFactoryPlus<ShoppingListItemDto> {

	public ShoppingListItemFactory() {
		super(ShoppingListItemDto.class);
	}
	
	/**
	 * Adds a new product to the shopping list
	 * 
	 * @param shoppingListKey
	 * @param product
	 * @param quantity
	 * @return The item order with the correct quantity ordered set.
	 */
	public ShoppingListItemDto createOrder(Key<ShoppingListDto> shoppingListKey, ProductDto product, int quantity) {
		ShoppingListItemDto item = new ShoppingListItemDto();
		item.setDateAdded(new Date());
		item.setProduct(product);
		item.setShoppingListKey(shoppingListKey);
		item.setQuantity(quantity);
		save( item );
		return item;
	}
	
	/**
	 * Updates a order on the shopping list incrementing the quantity of the ordered product.
	 * 
	 * @param item
	 * @param quantity
	 * @return The item order with the correct quantity ordered set.
	 */
	public ShoppingListItemDto updateOrder(ShoppingListItemDto item, int quantity) {
		item.setQuantity(item.getQuantity() + quantity);
		save (item);
		return item;
	}

	/**
	 * Search a shopping list for a product, and if it exists, returns the shopping list item
	 * that matches that query
	 * 
	 * @param shoppingListKey
	 * @param product
	 * @return
	 */
	public ShoppingListItemDto findProductInShoppingList(
			Key<ShoppingListDto> shoppingListKey, ProductDto product) {
		List<ShoppingListItemDto> items = ofy().load()
				.type(ShoppingListItemDto.class)
				.ancestor(shoppingListKey)
				.filter(ShoppingListItemDto.PRODUCT, product)
				.list();
		if(items!=null && !items.isEmpty()) {
			return items.get(0);
		} else {
			return null;
		}
	}
	
	

}
