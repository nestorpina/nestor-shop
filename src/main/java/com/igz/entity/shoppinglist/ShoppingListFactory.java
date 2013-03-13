package com.igz.entity.shoppinglist;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igzcode.java.gae.pattern.AbstractFactory;

public class ShoppingListFactory extends AbstractFactory<ShoppingListDto> {

    protected ShoppingListFactory() {
        super(ShoppingListDto.class);
    }
    
    /**
     * Get the shopping list items associated to a list
     * 
     * @param shoppingListId
     * @return
     */
    public List<ShoppingListItemDto> getShoppingListItems(Long shoppingListId) {
		return ofy().load().type(ShoppingListItemDto.class)
				.ancestor(Key.create(ShoppingListDto.class, shoppingListId))
				.list();
    }
}