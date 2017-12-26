package com.digitusrevolution.rideshare.user;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.digitusrevolution.rideshare.common.util.RESTClientImpl;

public class RSAllSystemDataLoader {
	
	public static void main(String[] args) {
		
		RESTClientImpl<Object> restClientImpl = new RESTClientImpl<>();
		String url = "http://localhost:8080/RSUserSystem/api/domain/loaddata/user";
		URI uri = UriBuilder.fromUri(url).build();
		restClientImpl.get(uri);
		
		url = "http://localhost:8080/RSServiceProviderSystem/api/domain/loaddata/serviceprovider";
		uri = UriBuilder.fromUri(url).build();
		restClientImpl.get(uri);
		
		url = "http://localhost:8080/RSRideSystem/api/domain/loaddata/ride";
		uri = UriBuilder.fromUri(url).build();
		restClientImpl.get(uri);		
		
	}

}
