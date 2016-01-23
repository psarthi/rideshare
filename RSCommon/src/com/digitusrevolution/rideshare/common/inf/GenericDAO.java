package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface GenericDAO<E,ID> {
	
	ID create(E entity);
	E get(ID id);
	E update(E entity);
	void delete(E entity);
	List<E> getAll();
}
