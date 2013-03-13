package com.igz.entity.shoppinglist;

import java.util.logging.Logger;

import com.igz.entity.product.ProductDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemManager;

public class ShoppingListManager extends ShoppingListFactory {
	
	private static final Logger LOGGER = Logger.getLogger(ShoppingListManager.class.getName());

	public void addProduct(ShoppingListDto shoppingList, ProductDto product) { 
		addProduct(shoppingList, product, 1);
	}

	/**
	 * 
	 * @param shoppingList
	 * @param product
	 * @param quantity
	 * 
	 * @throws IllegalArgumentException - if shoppingList is not saved
	 */
	public void addProduct(ShoppingListDto shoppingList, ProductDto product, int quantity ) {
		if(shoppingList.getId() == null) {
			throw new IllegalArgumentException("Shopping List must have an id (saved state)");
		}
		LOGGER.info(String.format("%s: Adding product %s(%d) to %s list", 
					shoppingList.getOwner().getName(), product.getName(), quantity, shoppingList.getName()));
		final ShoppingListItemManager shoppingListItemM = new ShoppingListItemManager();
		shoppingListItemM.createOrder(shoppingList.getKey(), product, quantity);
		save (shoppingList);
	}
}
