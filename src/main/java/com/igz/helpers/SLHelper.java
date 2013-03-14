package com.igz.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.igz.entity.user.UserDto;

/**
 * Helper for common methods used in servlets and filters. Also manages objects in session.
 * 
 * @author alejandro
 *
 */
public class SLHelper {

	//CONSTANTS FOR SESSION OBJECTS
	public static final String NAMESPACE = "NAMESPACE";
	public static final String USER_EMAIL = "USER_EMAIL";
	public static final String WHITE_LIST = "WHITE_LIST";
	public static final String USER = "USER";
	public static final String EMAIL_JOIN_REQUEST_BODY = "EMAIL_JOIN_REQUEST_BODY";
	public static final String EMAIL_JOIN_REQUEST_SUBJECT = "EMAIL_JOIN_REQUEST_SUBJECT";
	public static final String EMAIL_JOIN_REQUEST_FROM = "EMAIL_JOIN_REQUEST_FROM";
	public static final String CALLBACK_REQUEST = "JOIN_REQUEST";
	public static final String JOIN_REQUEST_PARAMS = "JOIN_REQUEST_PARAMS";
	public static final String DOMAIN_COUNTDOWN_DAYS = "DOMAIN_COUNTDOWN_DAYS";
	public static final String DOMAIN_GCOUNTDOWN_DAYS = "DOMAIN_GCOUNTDOWN_DAYS";
	
	
	//CONSTANTS FOR PATHS
	public static final String ERROR_PAGE = "error.html";
	public static final String LOGIN_PAGE = "login.html";
	public static final String INDEX_PAGE = "index.html";
	public static final String FORBIDEN_PAGE = "forbiden.html";
	public static final String CALLBACK_SERVLET = "/callback";
	
	/**
	 * invalidate the session.
	 * 
	 * @param p_session
	 */
	public static void clearSession( HttpSession p_session ){
		p_session.invalidate();
	}
	
	/**
	 * Overload for forceLogoutAndRedirectTo
	 * 
	 * @param p_session
	 * @param p_response
	 * @throws IOException
	 */
	public static void forceLogout( HttpSession p_session, HttpServletResponse p_response ) throws IOException{
		forceLogoutAndRedirectTo( p_session, p_response, LOGIN_PAGE );
	}

	/**
	 * Invalidate the session and redirect the response to the login page.
	 * 
	 * @param p_session
	 * @param p_response
	 * @throws IOException
	 */
	public static void forceLogoutAndRedirectTo( HttpSession p_session, HttpServletResponse p_response, String p_redirect ) throws IOException{
		SLHelper.clearSession( p_session );
		p_response.sendRedirect( p_redirect );
	}
	
	/**
	 * Retrieves a map with the query string parameters.
	 * 
	 * @param query
	 * @return
	 */
	public static Map<String, String> getQueryMap(String query) {
		query = query.replace("?", "");
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			map.put(name, value);
		}
		return map;
	}
	
	/**
	 * Checks if there is a valid user logged in the system.
	 * 
	 * Check for a valid namespace in session
	 * Check for valid domain user in session
	 * Check for a valid user entity in session
	 * 
	 * @param p_session
	 * @param p_response
	 * 
	 * @return Boolean
	 */
	public static Boolean validUserInSession( HttpSession p_session, HttpServletResponse p_response ){
		
		UserDto user = (UserDto) p_session.getAttribute( SLHelper.USER ); 
		if( user == null || !validUserInDomain(p_session, p_response) ){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks for a valid user in Domain.
	 * 
	 * Check for a valid namespace in session
	 * Check for valid domain user in session
	 *
	 * @param p_session
	 * @param p_response
	 * @return
	 */
	public static Boolean validUserInDomain( HttpSession p_session, HttpServletResponse p_response ){
		return true;
	}
		
	
	/**
	 * Return the user object stored in the session.
	 * 
	 * @param p_request
	 * @return
	 */
	public static UserDto getUser( HttpServletRequest p_request ){
    	return (UserDto) p_request.getSession().getAttribute( SLHelper.USER );
    }
	
	}
