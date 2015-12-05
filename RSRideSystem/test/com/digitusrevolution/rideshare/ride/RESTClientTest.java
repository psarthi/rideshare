package com.digitusrevolution.rideshare.ride;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.PropertyReader;
import com.digitusrevolution.rideshare.common.RESTClientImpl;
import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.ride.dto.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.GoogleGeocode;
import com.digitusrevolution.rideshare.ride.dto.Route;
import com.digitusrevolution.rideshare.ride.dto.Step;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RESTClientTest {

	public static void main (String args[]) throws ClassNotFoundException{

		//Load Class
		Class.forName("com.digitusrevolution.rideshare.common.PropertyReader");

	/*
	 
		// Get Message 
		 
		int id = 1;
		RESTClientUtil restClientUtil = new RESTClientUtil();
		User user1 = restClientUtil.getUser(id);
		System.out.println("FirstName: "+user1.getFirstName()+",Email: "+user1.getEmail());

		Collection<Role> roles = restClientUtil.getRoles(id);
		System.out.println("Role size: "+roles.size());


		//Post Message

		User user = new User();
		user.setEmail("email-9");
		City city = new City();
		city.setId(1);
		city.setName("Bangalore");
		user.setCity(city);

		String createUserURL = PropertyReader.getInstance().getProperty("POST_USER_URL");

		RESTClientImpl<User> restClientImpl = new RESTClientImpl<>();
		Response response = restClientImpl.post(createUserURL, user);
		String json = response.readEntity(String.class);
		System.out.println("Response: "+json);


*/
		
		// Converting JSON to Specific Class & Tree model using Jackson
	 
		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_URL");
		url = url.replace("{id}", Integer.toString(1));
		Response response1 = restClientUtil.get(url);

//		DummyDTO user = response1.readEntity(DummyDTO.class);
//		System.out.println("FirstName: "+user.getFirstName()+",Email: "+user.getEmail());
		
		String json1 = (String) response1.readEntity(String.class);
		System.out.println(json1);	
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			DummyDTO user1 = objectMapper.readValue(json1, DummyDTO.class);
			System.out.println("FirstName: "+user1.getFirstName()+",Email: "+user1.getEmail());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			JsonNode jsonNode = objectMapper.readValue(json1, JsonNode.class);
			
			String cityName = jsonNode.get("city").get("name").asText();
			System.out.println("City Name from JSON Tree: "+cityName);
			
			JsonNode roles = jsonNode.get("roles");
			
			for (JsonNode role : roles) {
				
				String roleName = role.get("name").asText();
				System.out.println("Role Name from Loop: " + roleName);
			}
			
			String role1 = roles.get(0).get("name").asText();
			String role2 = roles.get(1).get("name").asText();
			System.out.println("Role Name from JSON Tree: "+role1+","+role2);
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	

		
		RESTClientImpl<String> restClientImpl = new RESTClientImpl<>();
		String cordinateURL = "https://maps.googleapis.com/maps/api/geocode/json?address=Gopalan+Grandeur+Hoodi+Circle+Bangalore&key=AIzaSyATU4xHBUbuw9OqcmHJBTt7mxRvXO54DIg";
		String routeURL = "https://maps.googleapis.com/maps/api/directions/json?origin=Gopalan+Grandeur+Hoodi+Circle+Bangalore&destination=Silk+Board+Bangalore&key=AIzaSyATU4xHBUbuw9OqcmHJBTt7mxRvXO54DIg";
		Response response = restClientImpl.get(cordinateURL);
		String json = response.readEntity(String.class);
		System.out.println(json);
		
		response = restClientImpl.get(routeURL);
		String json2 = response.readEntity(String.class);
		System.out.println(json2);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			GoogleGeocode geocode = mapper.readValue(json,GoogleGeocode.class);
			System.out.println("Lat/Lang Value:" + geocode.getResults().get(0).getGeometry().getLocation().getLat()+","+geocode.getResults().get(0).getGeometry().getLocation().getLng());
			GoogleDirection direction = mapper.readValue(json2, GoogleDirection.class);
			List<Step> steps = direction.getRoutes().get(0).getLegs().get(0).getSteps();
			for (Step step : steps) {
				System.out.println("Start Location: " + step.getStartLocation().getLat()+","+step.getStartLocation().getLng());
				System.out.println("Distance: " + step.getDistance().getText());
				System.out.println("Time: " + step.getDuration().getText());
				System.out.println("End Location: " + step.getEndLocation().getLat()+","+step.getEndLocation().getLng());
				System.out.println("-----");
			}
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		

	}



}
