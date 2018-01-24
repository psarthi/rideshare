package com.digitusrevolution.rideshare.common.inf;

public interface DomainObjectPKLong<M> extends DomainObject<M>{

	long create(M model);

	M get(long id);

	M getAllData(long id);
	
	void delete(long id);

}
