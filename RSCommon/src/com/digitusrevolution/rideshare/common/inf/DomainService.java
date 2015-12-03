package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainService<T> {

	T get(int id, boolean fetchChild);

	List<T> getAll();
}