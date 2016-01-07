package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainObject<T>{
	
	List<T> getAll();

	void update(T model);
	
	/**
     * Fetch domain model child
     * 
     *<P>Sample code -
     * 
     *<P> user = userMapper.getDomainModelChild(user, userEntity);				
     * 
     */
	void fetchChild();
	
	
}