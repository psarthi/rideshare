package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainObject<M>{
	
	List<M> getAll();

	//IMP - Don't return the model even though DAO layer returns it, reason for returning is we are using merge instead of update. 
	//Standard update method doesn't return any thing. so we will follow the same standard
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