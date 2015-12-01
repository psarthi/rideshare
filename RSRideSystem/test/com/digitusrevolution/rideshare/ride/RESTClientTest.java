package com.digitusrevolution.rideshare.ride;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.PropertyReader;
import com.digitusrevolution.rideshare.common.RESTClientImpl;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RESTClientTest {
	
	public static void main (String args[]) throws ClassNotFoundException{
		
		//Load Class
		Class.forName("com.digitusrevolution.rideshare.common.PropertyReader");
		
		RESTClientImpl<User> restClient = new RESTClientImpl<>();
		String getUserURL = PropertyReader.getInstance().getProperty("GET_USER_DOMAIN_URL");
		int id = 1;
		//Below code is multiple replacement of string, first with {id} and second one is "1"
		getUserURL = getUserURL.replace("{id}", Integer.toString(id)).replace("1","2");
		String createUserURL = PropertyReader.getInstance().getProperty("CREATE_USER_URL");
		Response response;
		String json;
		
		response = restClient.get(getUserURL);
		User user1 = response.readEntity(User.class);
		System.out.println("FirstName: "+user1.getFirstName()+",Email: "+user1.getEmail());
		
		User user = new User();
		user.setEmail("email-9");
		City city = new City();
		city.setId(1);
		city.setName("Bangalore");
		user.setCity(city);
		
		response = restClient.post(createUserURL, user);
		json = response.readEntity(String.class);
		System.out.println("Response: "+json);
				
	}
	


}
