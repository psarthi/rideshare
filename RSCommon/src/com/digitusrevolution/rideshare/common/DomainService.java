package com.digitusrevolution.rideshare.common;

import java.util.List;

public interface DomainService<T> {

	int create(T model);

	T get(int id);

	/**
	 * {@inheritDoc}
	 * 
	 *  Don't try to call getUser to avoid duplicate code, else you would loose persistent 
	 *  entity object which is required for lazy fetch
	 */
	T getChild(int id);

	List<T> getAll();

	void update(T model);

	void delete(T model);

}