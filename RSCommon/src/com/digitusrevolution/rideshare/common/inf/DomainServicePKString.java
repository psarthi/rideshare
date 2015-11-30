package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainServicePKString<T> {
	
	String create(T model);

	T get(String id);

	/**
	 *  Don't try to call getUser to avoid duplicate code, else you would loose persistent 
	 *  entity object which is required for lazy fetch. 
	 *  
	 *<P> Call fetchChild method to load child
	 * 	
	 */
	T getChild(String id);

	List<T> getAll();

	void update(T model);

	void delete(T model);


}
