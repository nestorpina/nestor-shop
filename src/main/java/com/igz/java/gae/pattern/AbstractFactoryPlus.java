package com.igz.java.gae.pattern;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.igzcode.java.gae.pattern.AbstractFactory;

public abstract class AbstractFactoryPlus<DtoType> extends AbstractFactory<DtoType> {

	protected AbstractFactoryPlus(Class<DtoType> p_class) {
		super(p_class);
	}
	
    public DtoType getByKey(Key<DtoType> p_commentK ){
    	return ofy().load().key( p_commentK ).get();
    }

}
