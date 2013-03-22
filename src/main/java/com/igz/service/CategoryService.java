package com.igz.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.igz.entity.category.CategoryDto;
import com.igz.entity.category.CategoryManager;
import com.igz.exception.IgzException;

/**
 * Categories servlet
 * 
 * GET /category/all		getAllCategories
 * GET /category/{id}		getCategory(String)
 * GET /category/id/{id}	getCategory(Long)
 * POST /category			postCategory(Params...)
 *
 */
@Path("/category")
public class CategoryService {

    private final CategoryManager categoryM = new CategoryManager();
    
    /**
     * Get all the categories
     * 
     * @return SC_OK
     */
    @GET
    @Path("/all")
    @Produces("application/json;charset=UTF-8")
    public Response getAllCategories( @Context HttpServletRequest p_request ) {
    	
    	List<CategoryDto> list = categoryM.findAll();

    	if(list == null || list.isEmpty()) {
    		return Response.noContent().build();
    	} else {
    		return Response.ok().entity( new Gson().toJson( list ) ).build();
    	}
    }    
    
    /**
     * Get category by by webSafeString id
     * 
     * @return SC_OK
     */
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getCategory( @PathParam("id") String categoryId, @Context HttpServletRequest p_request ) {
    	
    	CategoryDto category = categoryM.getByKeyString(categoryId);

    	if(category == null) {
    		throw new WebApplicationException(Status.NOT_FOUND);
    	} else {
    		return Response.ok().entity( new Gson().toJson( category ) ).build();
    	}
    }
    
    /**
     * Get category by id
     * 
     * @return SC_OK
     */
    @GET
    @Path("/id/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getCategory( @PathParam("id") Long categoryId, @Context HttpServletRequest p_request ) {
    	
    	CategoryDto category;
		try {
			category = categoryM.getByLongId(categoryId);
		} catch (IgzException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

   		return Response.ok().entity( new Gson().toJson( category ) ).build();
    }    
    
    /**
     * Post a category
     * @param name 
     * 
     * @return SC_OK
     */
    @POST
    @Produces("application/json;charset=UTF-8")
    public Response postCategory( 
    		@FormParam("name") String name, 
    		@Context HttpServletRequest p_request  ) throws IgzException {
    	
    	// TODO Parameters validation

    	CategoryDto category = new CategoryDto();
    	category.setCreationDate(new Date());
    	category.setName(name);
    	
		categoryM.save(category);

   		return Response.ok().entity( new Gson().toJson( category ) ).build();
    }        

}
