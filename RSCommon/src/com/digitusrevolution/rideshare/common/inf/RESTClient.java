package com.digitusrevolution.rideshare.common.inf;

import java.net.URI;

import javax.ws.rs.core.Response;

public interface RESTClient<T> {

	Response get(URI uri);
	Response post(URI uri, T model);
	Response put(URI uri, T model);
	Response delete(URI uri);
}
