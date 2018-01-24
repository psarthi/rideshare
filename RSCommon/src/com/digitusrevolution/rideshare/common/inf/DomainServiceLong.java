package com.digitusrevolution.rideshare.common.inf;

import java.util.List;

public interface DomainServiceLong<M> {

	M get(long id, boolean fetchChild);

	List<M> getAll();
}