package com.digitusrevolution.rideshare.common.inf;

import javax.ws.rs.core.Response;

public interface RESTClient<T> {

	Response get(String url);
	Response post(String url, T model);
	Response put(String url, T model);
	Response delete(String url);
}
