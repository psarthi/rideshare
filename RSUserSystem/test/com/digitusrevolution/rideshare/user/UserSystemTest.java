package com.digitusrevolution.rideshare.user;



import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserRegistrationService;
import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;
import com.digitusrevolution.rideshare.user.domain.UserDO;
import com.digitusrevolution.rideshare.user.domain.UserService;
import com.digitusrevolution.rideshare.user.domain.resource.UserResource;

public class UserSystemTest {

	public static void main(String args[]){
		
		User user = new User();
		user.setFirstName("1-firstName");
		user.setLastName("1-lastName");
		user.setEmail("1-email");

		Vehicle vehicle1 = new Vehicle();
		Vehicle vehicle2 = new Vehicle();
		user.getVehicles().add(vehicle1);
		user.getVehicles().add(vehicle2);
		
		UserDO userDO = new UserDO();		
		UserService userService = new UserService();
		userDO.addVehicle(vehicle1);
		userDO.addVehicle(vehicle2);
		userService.createUser(user);
		
	}
}
