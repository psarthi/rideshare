package com.digitusrevolution.rideshare.common;

public interface GenericDAO<T> {
	
	void create(T entity);
	T get(Integer id);
	void update(T entity);
	void delete(T entity);
}
