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

import com.google.appengine.api.NamespaceManager;

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
//		HttpServletResponse response = (HttpServletResponse) p_response;
//		HttpSession session = request.getSession();

		LOGGER.info(request.getRequestURL().toString() );
		NamespaceManager.set("intelygenz.com");
		
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
		
		p_filterChain.doFilter(p_request, p_response);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}