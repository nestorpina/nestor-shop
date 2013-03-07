package com.igz.entity.user;

import java.util.Date;
import java.util.logging.Logger;

import com.igz.exception.IgzException;
import com.igzcode.java.util.StringUtil;

public class UserManager extends UserFactory {

    private static final Logger LOGGER = Logger.getLogger(UserManager.class.getName());

	/**
	 * Checks if the given email is already registered in our system and register it if not.
	 * 
	 * If the email is not in the datastore, is the first time the user access the application we need to store it for future use.
	 * 
	 * This method receive an UserDTO object with properties from the oAuthProvider and actualize the user in our system with them.
	 * 
	 * oAuthProvider properties that will be always updated (this mean that MUST be present in the domain user object: 
	 * - fullname
	 * 
	 * 
	 * @param p_email		Email of the user you want to recover
	 * @param p_domainUser  UserDto object with properties you want to be actualized in the system.
	 * @return
	 * @throws IgzException
	 */
	public UserDto loginUser( String p_email, UserDto p_remoteUser ) throws IgzException {
		
		if( !StringUtil.validateEmail( p_email ) ){
			throw new IgzException( IgzException.IGZ_ERR_IN_LOGIN, "Email null or empty" );
		}
		
		UserDto user = get( p_email );
		if( user == null ){
			LOGGER.info("new user in system with e=" + p_email + " n=" + p_remoteUser.getFullname());
			user = new UserDto();
			user.setEmail( p_email );
			user.setFullname( p_remoteUser.getFullname() );
			user.setCreated(new Date());
			save( user );
			
		} else {
			LOGGER.info("user already in system update n=" + p_remoteUser.getFullname() );
			user.setLastLogin( new Date() );
			user.setFullname( p_remoteUser.getFullname() );
			save( user );
		}
		
		return user;
	}
	
	/**
	 * Returns a user entity.
	 * 
	 * @param p_email
	 * @return
	 * @throws IgzException	IGZ_INVALID_EMAIL,IGZ_USER_NOT_IN_SYSTEM
	 */
	public UserDto getUser( String p_email ) throws IgzException{
		if( StringUtil.isNullOrEmpty(p_email) ){
			throw new IgzException( IgzException.IGZ_INVALID_EMAIL );
    	}
		
		UserDto user = get( p_email );
		if( user == null ){
			throw new IgzException( IgzException.IGZ_USER_NOT_IN_SYSTEM );
		}
		
		return user;
	}

}
