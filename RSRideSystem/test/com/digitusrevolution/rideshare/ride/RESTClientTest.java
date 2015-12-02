package com.digitusrevolution.rideshare.ride;

import java.util.Collection;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.PropertyReader;
import com.digitusrevolution.rideshare.common.RESTClientImpl;
import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RESTClientTest {
	
	public static void main (String args[]) throws ClassNotFoundException{
		
		//Load Class
		Class.forName("com.digitusrevolution.rideshare.common.PropertyReader");
		
		int id = 1;
		RESTClientUtil restClientUtil = new RESTClientUtil();
		User user1 = restClientUtil.getUser(id);
		System.out.println("FirstName: "+user1.getFirstName()+",Email: "+user1.getEmail());

		Collection<Role> roles = restClientUtil.getRoles(id);
		System.out.println("Role size: "+roles.size());

		
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
				
	}
	


}
