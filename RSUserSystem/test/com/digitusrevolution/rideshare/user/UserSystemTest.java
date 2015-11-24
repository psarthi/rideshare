package com.digitusrevolution.rideshare.user;



import java.util.List;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserRegistrationService;
import com.digitusrevolution.rideshare.user.domain.UserDO;
import com.digitusrevolution.rideshare.user.domain.UserService;
import com.digitusrevolution.rideshare.user.domain.resource.UserResource;

public class UserSystemTest {

	public static void main(String args[]){
		
		//create User from Business Service
		UserRegistrationService userRegistrationService = new UserRegistrationService();
		User user5 = new User();
		user5.setFirstName("Partha from Business Service");
		user5.setEmail("partha.sarthi-1@gmail.com");
		userRegistrationService.registerUser(user5);

		//Fetch User from Domain Resource
		UserResource userResource = new UserResource();
		Response response = userResource.getUser(1);
		User user2 = (User)response.getEntity();
		System.out.println("User details Fetch from UserService- "+user2.getId()+","+user2.getFirstName()+","+user2.getEmail());

		/*		
		//create User
		UserService userService = new UserService();
		User user1 = new User();
		user1.setFirstName("Partha");
		user1.setEmail("partha.sarthi@gmail.com");
		userService.createUser(user1);
	
		//create User
		User user4 = new User();
		user4.setFirstName("Puja");
		user4.setEmail("puja.sarthi@gmail.com");
		userService.createUser(user4);
		
		//Check User Exist
		System.out.println("User Exist status partha.sarthi@gmail.com - "+userService.checkUserExist("partha.sarthi@gmail.com"));
		System.out.println("User Exist status partha.sarthi@yahoo.com - "+userService.checkUserExist("partha.sarthi@yahoo.com"));
		
		//Fetch User
		UserService userService = new UserService();
		User user2 = userService.getUser(1);
		System.out.println("User details Fetch from UserService- "+user2.getId()+","+user2.getFirstName()+","+user2.getEmail());
		
		//Fetch all Users
		List<User> users = userService.getAllUser();
		for (User user : users) {
			System.out.println("User details: "+"id: "+user.getId()+" Name: "+user.getFirstName()+" Email: "+user.getEmail());
		}
				
		//Add vehicle
		Vehicle vehicle = new Vehicle();
		UserDO userDO = new UserDO();
		userDO.addVehicle(vehicle);
*/		
	}
}
