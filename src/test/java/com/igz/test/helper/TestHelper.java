package com.igz.test.helper;

import com.google.appengine.api.NamespaceManager;
import com.googlecode.objectify.ObjectifyService;
import com.igz.entity.user.UserDto;
import com.igz.entity.user.UserManager;

public class TestHelper {

	
	public static void prepareDS(){
		NamespaceManager.set("test");
		
        ObjectifyService.register(UserDto.class);

		UserManager userM = new UserManager();
		UserDto user = new UserDto();

		user.setEmail("alejandro.gonzalez@intelygenz.com");
		user.setFullname( "Alejando González" );
		userM.save( user );
	}
	
}
