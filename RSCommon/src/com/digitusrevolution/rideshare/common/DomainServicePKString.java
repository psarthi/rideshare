package com.digitusrevolution.rideshare.common;

import java.util.List;

public interface DomainServicePKString<T> {
	
	String create(T model);

	T get(String id);

	T getChild(String id);

	List<T> getAll();

	void update(T model);

	void delete(T model);


}
