package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainService<M> {

	M get(int id, boolean fetchChild);

	List<M> getAll();
}