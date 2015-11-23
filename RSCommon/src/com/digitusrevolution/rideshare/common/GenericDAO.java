package com.digitusrevolution.rideshare.common;

import java.util.List;

public interface GenericDAO<T> {
	
	int create(T entity);
	T get(int id);
	void update(T entity);
	void delete(T entity);
	List<T> getAll();
}
