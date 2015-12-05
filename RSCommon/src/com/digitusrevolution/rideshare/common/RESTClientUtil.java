package com.digitusrevolution.rideshare.common;

import java.util.Collection;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

/**
 * 
 * This Utility class will do all ground work to call REST services
 * and convert the response to appropriate model
 *
 */
public class RESTClientUtil {

	public User getUser(int id){

		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_URL");
		url = url.replace("{id}", Integer.toString(id));
		Response response = restClientUtil.get(url);
		User user = response.readEntity(User.class);

		return user;

	}

	public Collection<Role> getRoles(int id){

		RESTClientImpl<Role> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_ROLE_URL");
		url = url.replace("{id}", Integer.toString(id));
		Response response = restClientUtil.get(url);		
		Collection<Role> roles = response.readEntity(new GenericType<Collection<Role>>() {});
		return roles;

	}

	public Vehicle getVehicle(int userId, int vehicleId){

		RESTClientImpl<Vehicle> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VEHICLE_URL");
		url = url.replace("{userId}", Integer.toString(userId)).replace("{vehicleId}", Integer.toString(vehicleId));
		Response response = restClientUtil.get(url);
		Vehicle vehicle = response.readEntity(Vehicle.class);
		return vehicle;

	}
}
