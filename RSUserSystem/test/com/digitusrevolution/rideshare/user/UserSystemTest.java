package com.digitusrevolution.rideshare.user;



import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;

public class UserSystemTest {

	public static void main(String args[]){

		UserBusinessService userBusinessService = new UserBusinessService();
		User user = new User();
		
		user.setFirstName("1-firstName");
		user.setLastName("1-lastName");
		user.setEmail("1-email");

		Vehicle vehicle1 = new Vehicle();
		Vehicle vehicle2 = new Vehicle();

		user = userBusinessService.createUser(user);
		System.out.println("User id: " + user.getId());
		
		User user2 = new User();
		user2.setId(user.getId());
		user2 = userBusinessService.addVehicle(user2, vehicle1);
		user2 = userBusinessService.addVehicle(user2, vehicle2);
		System.out.println(user2.getVehicles().size());
		
		user = userBusinessService.getUserFullDetail(1);
		System.out.println(user.getVehicles().size());


		
	}
}
