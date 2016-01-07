package com.digitusrevolution.rideshare.common.inf;

public interface DomainObjectPKString<T> extends DomainObject<T>{
	
	String create(T model);

	T get(String name);

	T getChild(String name);	
	
	void delete(String name);
	
}