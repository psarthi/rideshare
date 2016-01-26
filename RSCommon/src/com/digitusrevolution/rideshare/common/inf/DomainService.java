package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainService<M> {

	M get(int id);

	List<M> getAll();
}