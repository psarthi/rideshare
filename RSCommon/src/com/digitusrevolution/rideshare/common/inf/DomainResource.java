package com.digitusrevolution.rideshare.common.inf;

import javax.ws.rs.core.Response;

public interface DomainResource<T> {

	Response get(int id, String fetchChild);

	Response getAll();
}