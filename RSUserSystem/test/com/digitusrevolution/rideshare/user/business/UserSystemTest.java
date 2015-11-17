package com.digitusrevolution.rideshare.user.business;



import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.UserDO;
import com.digitusrevolution.rideshare.user.domain.UserService;
import com.digitusrevolution.rideshare.user.ext.service.UserSystemExtService;

public class UserSystemTest {

	public static void main(String args[]){
		
		UserSystemExtService userSystemExtService = new UserSystemExtService();
		User user = userSystemExtService.getUser(2);
		System.out.println("User details - "+user.getId()+","+user.getFirstName()+","+user.getLastName()+","+user.getEmail());
		
		UserService userService = new UserService();
		userService.createUser(user);
		
		Vehicle vehicle = new Vehicle();
		vehicle.setId(10);
		UserDO userDO = new UserDO();
		userDO.addVehicle(vehicle);
		
	}
}
