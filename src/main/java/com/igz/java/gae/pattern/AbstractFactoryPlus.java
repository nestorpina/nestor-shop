package com.igz.java.gae.pattern;

import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.igzcode.java.gae.pattern.AbstractFactory;
import com.igzcode.java.util.StringUtil;

/**
 * Class that extends AbstractFactory<DtoType> to provide additional helper methods to 
 * work with model objects using objetify
 * 
 * @author nestor.pina
 *
 * @param <DtoType>
 */
public abstract class AbstractFactoryPlus<DtoType> extends AbstractFactory<DtoType> {

	protected AbstractFactoryPlus(Class<DtoType> p_class) {
		super(p_class);
	}
	
    public DtoType getByKey(Key<DtoType> key ){
    	return ofy().load().key( key ).get();
    }
    
    public void deleteByKey(Key<DtoType> key ){
    	ofy().delete().key( key ).now();
    }

	public DtoType getByKeyString(String websafeStringKey) {
		if( StringUtil.isNullOrEmpty( websafeStringKey )){
			throw new IllegalArgumentException("key must not be null or empty");
		} else {
	    	Key<DtoType> key = Key.create( websafeStringKey );
	    	return getByKey( key );
		}
	}
	
}
