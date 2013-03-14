package com.igz.entity.shoppinglist;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.VoidWork;
import com.googlecode.objectify.Work;
import com.igz.entity.product.ProductDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemManager;
import com.igz.exception.IgzException;

public class ShoppingListManager extends ShoppingListFactory {
	
	private static final Logger LOGGER = Logger.getLogger(ShoppingListManager.class.getName());

	/**
	 * Add a product to the shoppingList
	 * 
	 * If the product is already on the list, increment the quantity of the order by one
	 * 
	 * @param shoppingList
	 * @param product
	 * 
	 * @throws IllegalArgumentException - if shoppingList is not saved
	 * @return ShoppingListItemDto - The shoppingListItem created or incremented
	 * 
	 */
	public ShoppingListItemDto addProduct(ShoppingListDto shoppingList, ProductDto product) { 
		return addProduct(shoppingList, product, 1);
	}

	/**
	 * Add a product to the shoppingList specifying the quantity
	 * 
	 * If the product is already on the list, increment the quantity of the order by the 
	 * quantity specified
	 * 
	 * @param shoppingList
	 * @param product
	 * @param quantity
	 * 
	 * @throws IllegalArgumentException - if shoppingList is not saved
	 * @return ShoppingListItemDto - The shoppingListItem created or incremented
	 */
	public ShoppingListItemDto addProduct(final ShoppingListDto shoppingList, final ProductDto product, final int quantity ) {
		if(shoppingList.getId() == null) {
			throw new IllegalArgumentException("Shopping List must have an id (saved state)");
		}
		LOGGER.info(String.format("%s: Adding product %s(%d) to %s list", 
					shoppingList.getOwner().getName(), product.getName(), quantity, shoppingList.getName()));
		
		return ofy().transact(new Work<ShoppingListItemDto>() {
			@Override
			public ShoppingListItemDto run() {
				final ShoppingListItemManager shoppingListItemM = new ShoppingListItemManager();
				ShoppingListItemDto shoppingListItem = shoppingListItemM.createOrder(shoppingList.getKey(), product, quantity);
				save (shoppingList);
				return shoppingListItem;
			}
		});
	}

	/**
	 * Removes a item from the shopping list. 
	 * 
	 * @param listId
	 * @param itemId
	 */
	public void removeProduct(final Long listId, final Long itemId) {
		ofy().transact(new VoidWork() {
			@Override
			public void vrun() {
				Key<ShoppingListItemDto> key = Key.create(Key.create(ShoppingListDto.class, listId), ShoppingListItemDto.class, itemId);
				ofy().delete().key(key);
			}
		});
	}
	
	/**
	 * Sets the quantity of a product in the shopping list to the specified quantity.
	 * If the quantity is set to 0, this is the same as calling removeProduct 
	 * 
	 * @param listId
	 * @param itemId
	 * @param quantity
	 * @throws IgzException - If any parameter is null or quantity < 0
	 */
	public void setProductQuantity(final Long listId, final Long itemId, final Integer quantity) throws IgzException {
		if(listId == null || itemId == null || quantity == null || quantity.intValue() < 0) {
			throw new IllegalArgumentException("Shopping List must have an id (saved state)");
		}
		// TODO : Transaction, but with exceptions?
		final ShoppingListItemManager shoppingListItemM = new ShoppingListItemManager();
		Key<ShoppingListItemDto> key = Key.create(Key.create(ShoppingListDto.class, listId), ShoppingListItemDto.class, itemId);
		ShoppingListItemDto item = shoppingListItemM.getByKey(key);
		if(item==null) {
			throw new IgzException(IgzException.IGZ_SHOPPING_LIST_ITEM_NOT_FOUND);
		}
		if(quantity.intValue() == 0) {
			removeProduct(listId, itemId);
		} else {
			item.setQuantity(quantity);
			shoppingListItemM.save(item);
		}
		
	}
}
