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
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.user.UserDto;
import com.igz.exception.IgzException;

/**
 * Products servlet
 * 
 * GET /product/all			getAllProducts
 * GET /product/{id}		getProduct(String)
 * GET /product/id/{id}		getProduct(Long)
 *
 */
@Path("/product")
public class ProductService {

	private static final Logger LOGGER = Logger.getLogger(ShoppingListManager.class.getName());
    private final ProductManager productM = new ProductManager();
    
    /**
     * Get all the products
     * 
     * @return SC_OK
     */
    @GET
    @Path("/all")
    @Produces("application/json;charset=UTF-8")
    public Response getAllProducts( @Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) (UserDto) p_request.getAttribute("USER");
    	LOGGER.info("/product/all :" + user.getKey());
    	
    	List<ProductDto> list = productM.findAll();

    	if(list == null || list.isEmpty()) {
    		return Response.noContent().build();
    	} else {
    		return Response.ok().entity( new Gson().toJson( list ) ).build();
    	}
    }    
    
    /**
     * Get product by by webSafeString id
     * 
     * @return SC_OK
     */
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getProduct( @PathParam("id") String productId, @Context HttpServletRequest p_request ) {
    	
    	ProductDto product = productM.getByKeyString(productId);

    	if(product == null) {
    		throw new WebApplicationException(Status.NOT_FOUND);
    	} else {
    		return Response.ok().entity( new Gson().toJson( product ) ).build();
    	}
    }
    
    /**
     * Get product by id
     * 
     * @return SC_OK
     */
    @GET
    @Path("/id/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getProduct( @PathParam("id") Long productId, @Context HttpServletRequest p_request ) {
    	
    	ProductDto product;
		try {
			product = productM.getByLongId(productId);
		} catch (IgzException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

   		return Response.ok().entity( new Gson().toJson( product ) ).build();
    }    

}
