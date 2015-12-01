package com.digitusrevolution.rideshare.common;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

/**
 * 
 * This Utility class is mainly for get functions over REST services.
 *
 */
public class RESTClientUtil {
	
	public static User getUser(int id){
		
		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_DOMAIN_URL");
		url = url.replace("{id}", Integer.toString(id));
		Response response = restClientUtil.get(url);
		User user = response.readEntity(User.class);
		return user;
		
	}

}
