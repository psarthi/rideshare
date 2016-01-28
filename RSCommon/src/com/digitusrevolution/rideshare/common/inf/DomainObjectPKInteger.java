package com.digitusrevolution.rideshare.common.inf;

public interface DomainObjectPKInteger<M> extends DomainObject<M>{

	int create(M model);

	M get(int id);

	M getChild(int id);
	
	void delete(int id);

}
