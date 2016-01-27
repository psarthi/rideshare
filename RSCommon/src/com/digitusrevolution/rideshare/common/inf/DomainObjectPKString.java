package com.digitusrevolution.rideshare.common.inf;

public interface DomainObjectPKString<M> extends DomainObject<M>{
	
	String create(M model);

	M get(String name);	
	
	M getWithEagerFetch(String name);
	
	void delete(String name);
	
}