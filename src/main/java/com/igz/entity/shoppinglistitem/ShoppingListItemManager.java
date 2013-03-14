package com.igz.entity.shoppinglistitem;

import com.googlecode.objectify.Key;
import com.igz.entity.shoppinglist.ShoppingListDto;

public class ShoppingListItemManager extends ShoppingListItemFactory {

	public Key<ShoppingListItemDto> getKey(Long listId, Long itemId) {
		return Key.create(Key.create(ShoppingListDto.class, listId), ShoppingListItemDto.class, itemId);
	}
}
