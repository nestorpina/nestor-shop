package com.igz.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.exception.IgzException;
import com.igzcode.java.util.Trace;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

/**
 * Shopping list servlet
 * 
 * GET /shoplist/all			getAllShoppingList
 * GET /shoplist/{id}			getShoppingList(String)
 * GET /shoplist/id/{id}		getAllShoppingList(Long)
 * POST /shoplist/item			putItemInShoppingList(Long)
 * POST /shoplist/item/remove	removeItemFromShoppingList
 * 
 *
 */
@Path("/shoplist")
public class ShoppingListService {

	private static final Logger LOGGER = Logger.getLogger(ShoppingListManager.class.getName());
    private final ShoppingListManager slM = new ShoppingListManager();
    private final ProductManager productM = new ProductManager();
    
    /**
     * GET /shoplist/all		getAllShoppingList
     * 
     * Get all the shopping lists of the user logged in the session
     * 
     * @return SC_OK
     */
    @GET
    @Path("/all")
    @Produces("application/json;charset=UTF-8")
    public Response getAllShoppingList( @Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) (UserDto) p_request.getAttribute("USER");
    	LOGGER.info("/shoppinglist/all :" + user.getKey());
    	
    	List<ShoppingListDto> list = slM.findByUser(user);

    	if(list == null || list.isEmpty()) {
    		return Response.noContent().build();
    	} else {
    		return Response.ok().entity( new Gson().toJson( list ) ).build();
    	}
    }    
    
    /**
     * GET /shoplist/{id}		getShoppingList(String)
     * 
     * Get shopping list by webSafeString id
     * 
     * @return SC_OK
     */
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getShoppingList( @PathParam("id") String listId, @Context HttpServletRequest p_request ) {
    	
    	ShoppingListDto sl = slM.getByKeyString(listId);

    	if(sl == null) {
    		throw new WebApplicationException(Status.NOT_FOUND);
    	} else {
    		String json = buildShoppingListJson(sl);
			return Response.ok().entity( json ).build();
    	}
    }
    
    /**
     * GET /shoplist/id/{id}	getAllShoppingList(Long)
     * 
     * Get shopping list by id, where the user logged in the session is the owner
     * 
     * @return SC_OK
     */
    @GET
    @Path("/id/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getShoppingList( @PathParam("id") Long listId, @Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) p_request.getAttribute("USER");
    	ShoppingListDto sl;
		try {
			sl = slM.getByUserAndId(user, listId);
		} catch (IgzException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		} 

		String json = buildShoppingListJson(sl);
   		return Response.ok().entity( json ).build();
    }    

    /**
     * POST /shoplist/item		putItemInShoppingList
     * 
     * Put an item in the shopping list
     * 
     * @param listId - long id of the shopping list associated to the session user
     * @param productId - product id
     * @param quantity - optional, 1 by default
     * @param p_request
     * @return
     */
    @POST
    @Path("/item")
    @Produces("application/json;charset=UTF-8")
    public Response putItemInShoppingList( 
    		@FormParam("listId") Long listId,
  			@FormParam("productId") Long productId,
  			@DefaultValue("1") @FormParam("quantity") Long quantity,
    		@Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) p_request.getAttribute("USER");

		try {
			ProductDto product = productM.getByLongId(productId);
			ShoppingListDto shoppingList = slM.getByUserAndId(user, listId);
			ShoppingListItemDto item = slM.addProduct(shoppingList, product, quantity.intValue());
			return Response.ok().entity( new Gson().toJson( item ) ).build();
		} catch (IgzException e) {
			return errorResponse(e);
		}
    }
    
    /**
     * POST /shoplist/item/remove		removeItemFromShoppingList
     * 
     * Remove an item from a shopping list
     * 
     * @param listId - long id of the shopping list associated to the session user
     * @param itemId - item of the list to remove
     * @param p_request
     * @return
     */
    @POST
    @Path("/item/remove")
    @Produces("application/json;charset=UTF-8")
    public Response removeItemFromShoppingList( 
    		@FormParam("listId") Long listId,
  			@FormParam("itemId") Long itemId,
    		@Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) p_request.getAttribute("USER");
		try {
			ShoppingListDto shoppingList = slM.getByUserAndId(user, listId);
			slM.removeProduct(shoppingList.getKey(), itemId);
			return Response.ok().build();
		} catch (IgzException e) {
			return errorResponse(e);
		}
    }    

	private Response errorResponse(IgzException e) {
		String error = Trace.error(e);
		LOGGER.severe(error);
		return Response.status( HttpServletResponse.SC_BAD_REQUEST).entity( e.toJsonError() ).build();
	}  
	
	private String buildShoppingListJson(ShoppingListDto sl) {
		List<ShoppingListItemDto> shoppingListItems = slM.getShoppingListItems(sl.getKey());
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("shoplist", sl);
		result.put("items", shoppingListItems);
		return new JSONSerializer().exclude("*.class","*.raw","*.root").transform(new DateTransformer("dd/MM/yyyy HH:mm:ss"), Date.class).deepSerialize(result);
	}	
}
