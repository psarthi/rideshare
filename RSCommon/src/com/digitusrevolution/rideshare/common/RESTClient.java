package com.digitusrevolution.rideshare.common;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RESTClient {
	
	public static void main (String args[]){
		
		String webServiceBusinessURI = "http://localhost:8080/RSUserSystem/api/business/users";
		String webServiceDomainURI = "http://localhost:8080/RSUserSystem/api/domain/users";
		RESTClient restClient = new RESTClient();
		User user = new User();
		user.setFirstName("Partha from REST Client");
		user.setEmail("partha.sarthi@gmail3.com");
		restClient.postData(webServiceBusinessURI, user);
		restClient.getData(webServiceDomainURI);	
	}
	
	public User getData(String webServiceURI){
		
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
		//webtarget.path can be used to append into uri
		WebTarget webTarget = client.target(webServiceURI).path("1");
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();

		//Below code could get entity JSON response as string
		//String json = response.readEntity(String.class);
		//System.out.println("JSON is -" + json);
		
		User user = response.readEntity(User.class);		
		System.out.println("User details - "+user.getId()+","+user.getFirstName()+","+user.getLastName()+","+user.getEmail());

		return user;
	}

	
	public void postData(String webServiceURI, User user){
		
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class));
		//webtarget.path can be used to append into uri
		WebTarget webTarget = client.target(webServiceURI);
		
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(user, MediaType.APPLICATION_JSON));		
		
		String json = response.readEntity(String.class);
		System.out.println("JSON is - " + json);
		System.out.println("Resonse Details - " + response.getStatus() +"/"+ response.getStatusInfo());		
	}
}
