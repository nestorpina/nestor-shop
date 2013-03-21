package com.igz.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.igz.entity.user.UserDto;

/**
 * Protects all services to be accesed without a valid user in session.
 * 
 * This filter check for a valid a valid user and domain user in session.
 * 
 * Mapped to: /s/*
 * 
 * @author alejandro
 *
 */
public class ServiceFilter implements Filter {

	protected static final Logger LOGGER = Logger.getLogger(ServiceFilter.class.getName());

	@Override
	public void doFilter(ServletRequest p_request, ServletResponse p_response, FilterChain p_filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) p_request;
		HttpServletResponse response = (HttpServletResponse) p_response;
		HttpSession session = request.getSession();

		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user != null) {
			UserDto userDto = new UserDto();
			userDto.setEmail(user.getEmail());
			userDto.setFullname(user.getNickname());
			session.setAttribute("USER", userDto);
	    }
		NamespaceManager.set("intelygenz.com");

		LOGGER.info(String.format("[%s] [%s]", user==null?"no-user":user.getEmail(), request.getRequestURL().toString() ));
		
//		String namespace = (String) session.getAttribute( NextInitHelper.NAMESPACE );
//		
//		DomainHelper domainH = NamespaceHelper.getDomainHelper( namespace );
//		UserDto userInSession = (UserDto) session.getAttribute( NextInitHelper.USER ); 
//		
//		if( !domainH.validUserInSession( session ) || userInSession == null ){
//			response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
//			LOGGER.info("No user in session when calling " + request.getPathInfo());
//			return;
//		}
		if(user == null) {
			response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
			return;
		}
		
		p_filterChain.doFilter(p_request, p_response);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}