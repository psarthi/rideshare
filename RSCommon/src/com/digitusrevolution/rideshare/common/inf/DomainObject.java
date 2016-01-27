package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainObject<M>{
	
	List<M> getAll();

	void update(M model);
	
}