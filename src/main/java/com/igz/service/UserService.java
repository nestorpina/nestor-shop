package com.igz.service;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;
import com.igz.exception.IgzException;
import com.igzcode.java.util.Trace;

/**
 * User servlet
 * 
 * GET /user/me		getMe
 * GET /user/all	getAll
 * GET /user/{id}	getUser
 * DELETE /user/notification/{id}	deleteNotification
 * GET /user/me/notifications getMeNotifications
 * 
 *
 */
@Path("/user")
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final UserManager userM = new UserManager();
    
    
    /**
     * Get info about user in the session.
     * 
     * @return SC_OK
     */
    @GET
    @Path("/me")
    @Produces("application/json;charset=UTF-8")
    public Response getMe( @Context HttpServletRequest p_request ) {
    	
    	UserDto user = (UserDto) p_request.getSession().getAttribute("USER");

    	return Response.ok().entity( new Gson().toJson( user ) ).build();
    }
    
    /**
     * Return all users in the system.
     * Used in live-user-searchs
     * 
     * 
     * @return SC_OK
     */
    @GET
    @Path("/all")
    @Produces("application/json;charset=UTF-8")
    public Response getUsers( @Context HttpServletRequest p_request ) {
    	
        List<UserDto> userL = userM.findAll();

        return Response.ok().entity( new Gson().toJson( userL ) ).build();
    }
    
    /**
     * Get user info.
     * 
     * @PathParam id
     * 
     * @return SC_BAD_REQUEST, SC_OK
     */
    @GET
    @Path("/{id}")
    @Produces("application/json;charset=UTF-8")
    public Response getUser(@PathParam("id") String p_email, @Context HttpServletRequest p_request ) {
    	UserDto user = null;
    	
    	try {
    		user = userM.getUser( p_email );
		} catch (IgzException e) {
			LOGGER.severe( Trace.error(e) );
			return Response.status( HttpServletResponse.SC_BAD_REQUEST ).entity( e.toJsonError() ).build();
		}
    	
        return Response.ok().entity( new Gson().toJson( user ) ).build();
    }
    

}
