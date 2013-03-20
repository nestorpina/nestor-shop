package com.igz.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.NamespaceManager;
import com.igz.entity.category.CategoryManager;
import com.igz.entity.product.ProductManager;
import com.igz.entity.shoppinglist.ShoppingListManager;
import com.igz.entity.shoppinglistitem.ShoppingListItemManager;
import com.igz.entity.user.UserManager;
import com.igz.exception.IgzException;
import com.igz.helpers.SLHelper;
import com.igz.helpers.TestHelper;
import com.igzcode.java.gae.configuration.ConfigurationManager.InvalidConfigurationException;
import com.igzcode.java.gae.util.ConfigUtil;
import com.igzcode.java.util.StringUtil;
import com.igzcode.java.util.Trace;

/**
 * 
 * Servlet to reset datastore per domain.
 * 
 * TODO: remove hard param
 * 
 * @author alejandro
 *
 */
public class DomainServlet  extends HttpServlet{

    private static final Logger LOGGER = Logger.getLogger(DomainServlet.class.getName());
	private static final long serialVersionUID = -1566941710523088445L;
	
	private static final String CONFIG_KEY_CREATED_DOMAIN = "APP_CREATED_DOMAIN";
	
	@Override
	protected void doGet(HttpServletRequest p_request, HttpServletResponse p_response) throws ServletException, IOException {
		
		String namespace =  p_request.getParameter("ns");
		String action = p_request.getParameter("action");
		String hard = p_request.getParameter("h") != null ? p_request.getParameter("h") : "";
		
		NamespaceManager.set("-global-");
		ConfigUtil config = ConfigUtil.getInstance();
		
		String whiteListS = ConfigUtil.getInstance().getValue( SLHelper.WHITE_LIST );
		
		if( StringUtil.isNullOrEmpty(namespace) || StringUtil.isNullOrEmpty(action) || whiteListS == null ){
			p_response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		LOGGER.info("NS="+namespace+" wl=" + whiteListS + " action=" + action + " h=" + hard );

		if( whiteListS.contains( namespace ) && action.equals( "reset" ) ){
			try {
				String dsKey = CONFIG_KEY_CREATED_DOMAIN + namespace.replace(".","");
				if( config.getValue( dsKey ) != null && !hard.equals( "hard" ) ){
					p_response.getWriter().println("WARNING: The domain has been created before. You must remove " + dsKey + " entity from ConfigurationDto to create it again.<br>This action will delete all info in the domain namespace and reset it.");
					return;
				}
				LOGGER.info( "SAVE CONF KEY " + dsKey );
				config.save( dsKey, new Date().toString() );
				
				LOGGER.info("RESET DOMAIN " + namespace );
				
				resetDomainAndcreateTestData( namespace );
				
			} catch (IgzException e) {
				LOGGER.severe( Trace.error(e) );
				p_response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} catch (InvalidConfigurationException e) {
				LOGGER.severe( Trace.error(e) );
				p_response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
			
			
		} else {
			p_response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	public void resetDomainAndcreateTestData( String p_namepsace ) throws IgzException{

		NamespaceManager.set( p_namepsace );
		
		UserManager userM = new UserManager();
		userM.deleteAll();
		CategoryManager categoryM = new CategoryManager();
		categoryM.deleteAll();
		ProductManager productM = new ProductManager();
		productM.deleteAll();
		ShoppingListItemManager sliM = new ShoppingListItemManager();
		sliM.deleteAll();
		ShoppingListManager slM = new ShoppingListManager();
		slM.deleteAll();
		
		TestHelper.loadTestData();

	}
	
    
}
