package com.igz.entity.shoppinglist;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.googlecode.objectify.Key;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.exception.IgzException;
import com.igz.java.gae.pattern.AbstractFactoryPlus;

public class ShoppingListFactory extends AbstractFactoryPlus<ShoppingListDto> {

    protected ShoppingListFactory() {
        super(ShoppingListDto.class);
    }
    
    /**
     * Get the shopping list items associated to a list
     * 
     * @param shoppingListKey
     * @return
     */
    public List<ShoppingListItemDto> getShoppingListItems(Key<ShoppingListDto> shoppingListKey) {
		return ofy().load().type(ShoppingListItemDto.class)
				.ancestor(shoppingListKey)
				.list();
    }
    
    public ShoppingListDto getByUserAndId(UserDto user, Long id) throws IgzException {
    	Key<ShoppingListDto> key = Key.create(user.getKey(), ShoppingListDto.class, id);
    	ShoppingListDto shoppingListDto = getByKey(key);
    	if(shoppingListDto == null) {
    		throw new IgzException(IgzException.IGZ_INVALID_SHOPPING_LIST);
    	}
    	return shoppingListDto;
    }
}
