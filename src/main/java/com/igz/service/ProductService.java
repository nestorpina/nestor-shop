package com.igz.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.igz.entity.category.CategoryDto;
import com.igz.entity.category.CategoryManager;
import com.igz.entity.product.ProductDto;
import com.igz.entity.product.ProductDto.UnitType;
import com.igz.entity.product.ProductManager;
import com.igz.exception.IgzException;
import com.igzcode.java.util.Trace;

/**
 * Products servlet
 * 
 * GET /product/all			getAllProducts
 * GET /product/{id}		getProduct(Long)
 * POST /product			postProduct(Params...)
 * POST /product/json		postProductJson(product)
 *
 */
@Path("/product")
public class ProductService {

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
    	
    	List<ProductDto> list = productM.findAll();

    	if(list == null || list.isEmpty()) {
    		return Response.noContent().build();
    	} else {
    		return Response.ok().entity( new Gson().toJson( list ) ).build();
    	}
    }    
    
    /**
     * Get product by long id
     * 
     * @return SC_OK
     */
    @GET
    @Path("/{id}")
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
    
    /**
     * Post a product
     * @param name
     * @param description
     * @param category
     * @param units
     * @param unitType
     * @param p_request
     * 
     * @return SC_OK
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    public Response postProduct( 
    		@FormParam("name") String name,
    		@FormParam("description") String description,
    		@FormParam("category") Long category,
    		@FormParam("units") Integer units, 
    		@FormParam("unitType") UnitType unitType, 
    		@Context HttpServletRequest p_request  ) {
    	
    	ProductDto product = new ProductDto();
    	product.setCreationDate(new Date());
    	product.setName(name);
    	product.setDescription(description);
    	product.setCategory(new CategoryDto(category));
    	product.setUnits(units);
    	product.setUnitType(unitType);
    	
		try {
			productM.saveProduct(product);
		} catch (IgzException e) {
			return Response.status( HttpServletResponse.SC_BAD_REQUEST).entity( e.toJsonError() ).build();
		}  
		

   		return Response.ok().entity( new Gson().toJson( product ) ).build();
    }        
    
    /**
     * Post a product
     * 
     * @return SC_OK
     * @throws IgzException IGZ_INVALID_CATEGORY if category not found
     */
    @POST
    @Path("/json")
    @Produces("application/json;charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postProductJSON( ProductDto product,
    		@Context HttpServletRequest p_request  ) throws IgzException {
    	
		try {
			productM.saveProduct(product);
		} catch (IgzException e) {
			return Response.status( HttpServletResponse.SC_BAD_REQUEST).entity( e.toJsonError() ).build();
		}  

   		return Response.ok().entity( new Gson().toJson( product ) ).build();
    }          

}
