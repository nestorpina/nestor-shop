package com.igz.service;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemManager;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;

/**
 * Shopping list servlet
 * 
 * GET /shoplist/all	getAllShoppingList
 * GET /shoplist/{id}	getShoppingList
 * 
 *
 */
@Path("/shoplist")
public class ShoppingListService {

    private static final Logger LOGGER = Logger.getLogger(ShoppingListService.class.getName());

    private final UserManager userM = new UserManager();
    private final ShoppingListManager slM = new ShoppingListManager();
    private final ShoppingListItemManager sliM = new ShoppingListItemManager();
    
    
    /**
     * Get info about user in the session.
     * 
     * @return SC_OK
     */
    @GET
    @Path("/all")
    @Produces("application/json;charset=UTF-8")
    public Response getAllShoppingList( @Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) p_request.getSession().getAttribute("USER");
    	
    	List<ShoppingListDto> list = slM.findAll();

    	if(list == null || list.isEmpty()) {
    		return Response.noContent().build();
    	} else {
    		return Response.ok().entity( new Gson().toJson( list ) ).build();
    	}
    }    
    
    /**
     * Get info about user in the session.
     * 
     * @return SC_OK
     */
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getShoppingList( @PathParam("id") String listId, @Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) p_request.getSession().getAttribute("USER");
    	
    	ShoppingListDto sl = slM.getByKeyString(listId);

    	if(sl == null) {
    		throw new WebApplicationException(Status.NOT_FOUND);
    	} else {
    		List<ShoppingListItemDto> shoppingListItems = slM.getShoppingListItems(sl.getKey());
    		return Response.ok().entity( new Gson().toJson( shoppingListItems ) ).build();
    	}
    }

}
