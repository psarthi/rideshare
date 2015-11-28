package com.digitusrevolution.rideshare.common;

import java.util.List;

public interface GenericDAO<T,ID> {
	
	ID create(T entity);
	T get(ID id);
	T update(T entity);
	void delete(T entity);
	List<T> getAll();
}
