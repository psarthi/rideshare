package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainService<T> {

	int create(T model);

	T get(int id, boolean fetchChild);

	List<T> getAll();

	void update(T model);

	void delete(T model);
	

}