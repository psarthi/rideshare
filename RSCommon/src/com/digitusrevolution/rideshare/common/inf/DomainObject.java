package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainObject<M>{
	
	List<M> getAll();

	void update(M model);
	
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