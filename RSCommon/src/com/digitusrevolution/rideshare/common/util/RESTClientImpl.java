package com.digitusrevolution.rideshare.common.util;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.inf.RESTClient;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class RESTClientImpl<T> implements RESTClient<T>{

	/*
	 * ***This is very important, JacksonJsonProvider is the implementation of
	 * MessageBodyWriter/Reader which is required for "readEntity" method, 
	 * else it would throw MessageBodyWriter/Reader not found exception
	 * 
	 * https://jersey.java.net/documentation/latest/message-body-workers.html#mbw.ex.client.mbr.reg
	 * 
	 * ****Registering of ObjectMapperContextResolver is important as we have registered JSR310 module there and without registering this, 
	 * Jersey client is not aware of JSR310 module, so it will not be able to de-serialize ZonedDateTime
	 *
	 * 
	 */
	private final Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class)).register(JacksonJsonProvider.class)
			.register(ObjectMapperContextResolver.class);
	private String token = AuthService.getInstance().getSystemToken();
	
	@Override
	public Response get(URI uri) {
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token);
		Response response = invocationBuilder.get();	
		
		return response;
	}

	@Override
	public Response post(URI uri, T model) {
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token);
		Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));		
		
		return response;
	}
	
	public Response post(URI uri, T model, MultivaluedMap<String, Object> headers) {
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
				.headers(headers);
		Response response = invocationBuilder.post(Entity.entity(model, MediaType.APPLICATION_JSON));		
		
		return response;
	}


	@Override
	public Response put(URI uri, T model) {
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token);
		Response response = invocationBuilder.put(Entity.entity(model, MediaType.APPLICATION_JSON));		
		
		return response;
	}

	@Override
	public Response delete(URI uri) {
		WebTarget webTarget = client.target(uri);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token);
		Response response = invocationBuilder.delete();		
		
		return response;
	}

}
