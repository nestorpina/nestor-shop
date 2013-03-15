package com.igz.entity.shoppinglistitem;

import com.googlecode.objectify.Key;
import com.igz.entity.shoppinglist.ShoppingListDto;

public class ShoppingListItemManager extends ShoppingListItemFactory {

	public Key<ShoppingListItemDto> getKey(Key<ShoppingListDto> listKey, Long itemId) {
		return Key.create(listKey, ShoppingListItemDto.class, itemId);
	}
}
