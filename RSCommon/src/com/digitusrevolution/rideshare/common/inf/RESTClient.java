package com.digitusrevolution.rideshare.common.inf;

import java.net.URI;

import javax.ws.rs.core.Response;

public interface RESTClient<M> {

	Response get(URI uri);
	Response post(URI uri, M model);
	Response put(URI uri, M model);
	Response delete(URI uri);
}
