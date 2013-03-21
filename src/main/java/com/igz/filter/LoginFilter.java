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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Checks if user is logged, and redirects to login if not
 * 
 * Mapped to: /
 * 
 * @author npina
 *
 */
public class LoginFilter implements Filter {

	protected static final Logger LOGGER = Logger.getLogger(LoginFilter.class.getName());

	@Override
	public void doFilter(ServletRequest p_request, ServletResponse p_response, FilterChain p_filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) p_request;
		HttpServletResponse response = (HttpServletResponse) p_response;

		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    LOGGER.info(String.format("[%s] [%s]", user==null?"no-user":user.getEmail(), request.getRequestURL().toString() ));
	    if (user == null) {
	    	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
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