package com.igz.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.NamespaceManager;
import com.googlecode.objectify.ObjectifyService;
import com.igz.entity.DatastoreObject;
import com.igz.entity.category.CategoryDto;
import com.igz.entity.product.ProductDto;
import com.igz.entity.shoppinglist.ShoppingListDto;
import com.igz.entity.shoppinglistitem.ShoppingListItemDto;
import com.igz.entity.user.UserDto;
import com.igz.exception.IgzException;
import com.igzcode.java.gae.configuration.ConfigurationDto;
import com.igzcode.java.gae.configuration.ConfigurationManager.InvalidConfigurationException;
import com.igzcode.java.gae.util.ConfigUtil;

public class InitServlet extends HttpServlet {
    
	private static final long serialVersionUID = 7003530042096811840L;

	/**
	 *  Es estática para que pueda ser invocada desde los test unitarios
	 * @throws InvalidConfigurationException 
	 */
    public static void initApp () {

		
    	ObjectifyService.register(ConfigurationDto.class);
        ObjectifyService.register(UserDto.class);
        ObjectifyService.register(CategoryDto.class);
        ObjectifyService.register(ProductDto.class);
        ObjectifyService.register(ShoppingListDto.class);
        ObjectifyService.register(ShoppingListItemDto.class);
        ObjectifyService.register(DatastoreObject.class);
        
        NamespaceManager.set("-global-");
        ConfigUtil config = ConfigUtil.getInstance();
        config.init();
        config.copyPropertiesIntoDatastore(true);
        
        //
        // Si se inicia la aplicación en desarrollo se rellena el datastore 
        // con los datos básicos que necesita la aplicación para funcionar
        //
        if ( config.isDev() ) {
        	CreateServlet.createApp();
        }

    }

    @Override
    public void init() throws ServletException {
        
        initApp();
        
        //
        // Si estamos en desarrollo se inicia la aplicación con datos de prueba
        //
        if ( ConfigUtil.getInstance().isDev() ) {
			createDevTestData();
        }
    }

    private void createDevTestData() {
    	try {
			NamespaceManager.set( "intelygenz.com" );
			
			new DomainServlet().resetDomainAndcreateTestData("intelygenz.com");
			
		} catch (IgzException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Refresca las variables de configuración según lo que se establezca en el datastore
     */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ConfigUtil config = ConfigUtil.getInstance();
		config.loadValuesFromDatastore();
		resp.getWriter().println("Configuration values has been loaded from datastore correctly.");
	}
    
}
