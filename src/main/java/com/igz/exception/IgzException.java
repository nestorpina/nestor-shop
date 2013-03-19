package com.igz.exception;

import java.util.HashMap;

public class IgzException extends Exception {

	private static final long serialVersionUID = 8484716012306489290L;

	//GPLUS
	public static final int IGZ_ERROR_GETTING_DOMAIN_PROFILE = 1002;
	
	//GENERAL
	public static final int IGZ_INVALID_EMAIL = 2000;
	public static final int IGZ_INVALID_GROUP = 2001;
	public static final int IGZ_USER_ALREADY_GROUP = 2002;
	public static final int IGZ_USER_NOT_IN_SYSTEM = 2003;
	public static final int IGZ_INVALID_USER = 2004;
	public static final int IGZ_NOT_FOUND = 2005;
	public static final int IGZ_USER_NOT_INVITED_GROUP = 2006;
	public static final int IGZ_ERROR_SENDING_EMAIL = 2007;
	public static final int IGZ_UNAUTHORIZED_USER = 2008;
	public static final int IGZ_INVALID_PARAMS = 2009;
	
	
	//LOGIN
	public static final int IGZ_ERR_IN_LOGIN = 3000;
	
	//NOTIFICACIONS
	public static final int IGZ_INVALID_NOTIFICATION_PARAMS = 3200;
	public static final int IGZ_ERROR_DELETING_NOTIFICATION = 3201;
	public static final int IGZ_SENDING_EMAIL_NOTIFICATION = 3202;
	
	//SHOPPING LIST
	public static final int IGZ_INVALID_SHOPPING_LIST_ITEM = 4001;
	public static final int IGZ_INVALID_PRODUCT = 4002;
	public static final int IGZ_INVALID_SHOPPING_LIST = 4003;
	
	
	private static final String IGZ_UNKNOWN_EXCEPTION_CODE = "Unknown exception code received. code=";
	
	private static final HashMap<Integer, String> CODE_MESSAGES;
	static {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		//GPLUS
	    map.put(IGZ_ERROR_GETTING_DOMAIN_PROFILE, "Cant get GPlus profile");

		//GENERAL
	    map.put(IGZ_INVALID_EMAIL, "Invalid email.");
	    map.put(IGZ_INVALID_GROUP, "Invalid group specified.");
	    map.put(IGZ_USER_ALREADY_GROUP, "User is already in that group.");
	    map.put(IGZ_USER_NOT_IN_SYSTEM, "User is not in the systemm yet.");
	    map.put(IGZ_INVALID_USER, "Invalid user entity.");
	    map.put(IGZ_NOT_FOUND, "Entity not found.");
	    map.put(IGZ_USER_NOT_INVITED_GROUP, "This user is not invited to that group.");
	    map.put(IGZ_ERROR_SENDING_EMAIL, "Error sending e-mail.");
	    map.put(IGZ_UNAUTHORIZED_USER, "User unauthorized.");
	    map.put(IGZ_INVALID_PARAMS, "User unauthorized.");
	    
		//LOGIN
	    map.put(IGZ_ERR_IN_LOGIN, "Cant log in with this user.");

	    //NOTIFICACIONS
	    map.put(IGZ_INVALID_NOTIFICATION_PARAMS, "Invalid notification params.");
	    map.put(IGZ_ERROR_DELETING_NOTIFICATION, "Error deleting a notification.");
	    map.put(IGZ_SENDING_EMAIL_NOTIFICATION, "Invalid params to send the email notification.");
	    
	    //SHOPPING LIST
	    map.put(IGZ_INVALID_SHOPPING_LIST_ITEM, "Shopping list item not found");
	    map.put(IGZ_INVALID_SHOPPING_LIST, "Shopping list not found");
	    map.put(IGZ_INVALID_PRODUCT, "Product not found");
	    
	    CODE_MESSAGES = map;
	}
	
	private int code;
	
	public IgzException( int p_code, String p_msg ) {
		super( CODE_MESSAGES.get( p_code ) == null ? IGZ_UNKNOWN_EXCEPTION_CODE + p_code : "ERR-" + p_code +" " + CODE_MESSAGES.get( p_code ) + ". " + p_msg );
        this.code = p_code;
	}
	
	public IgzException( int p_code ) {
        super( CODE_MESSAGES.get( p_code ) == null ? IGZ_UNKNOWN_EXCEPTION_CODE + p_code : "ERR-" + p_code +" " + CODE_MESSAGES.get( p_code ) );
        this.code = p_code;
    }
	
	public int getErrorCode(){
		return this.code;
	} 
	
	public String toJsonError(){
		return "{\"ERROR\":{\"CODE\":\""+this.code+"\",\"MSG\":\""+this.getMessage()+"\"}}";
	}

	public String toJsonMsg(){
		return "{\"MSG\":{\"CODE\":\""+this.code+"\",\"MSG\":\""+this.getMessage()+"\"}}";
	}
	
	
}
