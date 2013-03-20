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
	
	/**
	 * Adds a product to the shopping list, checking if it already exists in the list. 
	 * In that case it will increment the quantity of the ordered product.
	 * 
	 * @param shoppingListKey
	 * @param product
	 * @param quantity
	 * @return The item order with the correct quantity ordered set.
	 */
	public ShoppingListItemDto createOrder(Key<ShoppingListDto> shoppingListKey,
			ProductDto product, int quantity) {
		// Check if the item is already on the order list
		List<ShoppingListItemDto> items = findProductInShoppingList(shoppingListKey, product);
		for (ShoppingListItemDto item : items) {
			if(item.getProduct().getKey().equals(product.getKey())) {
				item.setQuantity(item.getQuantity() + quantity);
				save (item);
				return item;
			}
		}
		return put(shoppingListKey, product, quantity);
	}

	/**
	 * Search a shopping list for a product, and if it exists, returns the shopping list items
	 * that matches that query
	 * 
	 * @param shoppingListKey
	 * @param product
	 * @return
	 */
	private List<ShoppingListItemDto> findProductInShoppingList(
			Key<ShoppingListDto> shoppingListKey, ProductDto product) {
		List<ShoppingListItemDto> items = ofy().load()
				.type(ShoppingListItemDto.class)
				.ancestor(shoppingListKey)
				.filter(ShoppingListItemDto.PRODUCT, product)
				.list();
		return items;
	}
	
	

}
