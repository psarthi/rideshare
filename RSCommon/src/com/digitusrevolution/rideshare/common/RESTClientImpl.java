package com.digitusrevolution.rideshare.common;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import com.digitusrevolution.rideshare.common.inf.RESTClient;

public class RESTClientImpl<T> implements RESTClient<T>{


	@Override
	public Response get(URI uri) {
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
		WebTarget webTarget = client.target(uri);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();	
		
		return response;
	}

	@Override
	public Response post(URI uri, T model) {
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
		WebTarget webTarget = client.target(uri);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));		
		
		return response;
	}

	@Override
	public Response put(URI uri, T model) {
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
		WebTarget webTarget = client.target(uri);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.put(Entity.entity(model, MediaType.APPLICATION_JSON));		
		
		return response;
	}

	@Override
	public Response delete(URI uri) {
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
		WebTarget webTarget = client.target(uri);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.delete();		
		
		return response;
	}

}
