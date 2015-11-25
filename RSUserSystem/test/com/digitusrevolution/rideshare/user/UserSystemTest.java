package com.digitusrevolution.rideshare.user;



import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;

public class UserSystemTest {

	public static void main(String args[]){

		User user = new User();
		user.setFirstName("1-firstName");
		user.setLastName("1-lastName");
		user.setEmail("1-email");

		Vehicle vehicle1 = new Vehicle();
		Vehicle vehicle2 = new Vehicle();

		UserBusinessService userBusinessService = new UserBusinessService();
		user = userBusinessService.createUser(user);
		userBusinessService.addVehicle(user, vehicle1);
		userBusinessService.addVehicle(user, vehicle2);
		System.out.println(user.getVehicles().size());
	}
}
