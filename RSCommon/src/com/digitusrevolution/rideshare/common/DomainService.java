package com.digitusrevolution.rideshare.common;

import java.util.List;

public interface DomainService<T> {

	int create(T model);

	T get(int id);

	T getChild(int id);

	List<T> getAll();

	void update(T model);

	void delete(T model);

}