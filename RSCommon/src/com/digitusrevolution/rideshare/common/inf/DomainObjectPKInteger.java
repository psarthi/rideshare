package com.digitusrevolution.rideshare.common.inf;

public interface DomainObjectPKInteger<T> extends DomainObject<T>{

	int create(T model);

	T get(int id);

	T getChild(int id);

}
