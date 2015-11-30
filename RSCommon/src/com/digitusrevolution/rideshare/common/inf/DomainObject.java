package com.digitusrevolution.rideshare.common.inf;

public interface DomainObject {
	
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