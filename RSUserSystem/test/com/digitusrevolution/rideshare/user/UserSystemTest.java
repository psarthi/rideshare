package com.digitusrevolution.rideshare.user;



import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;

public class UserSystemTest {

	public static void main(String args[]){

		UserBusinessService userBusinessService = new UserBusinessService();
		User user = new User();
		City city = new City();
		city.setName("Bangalore");
		
		user.setFirstName("1-firstName");
		user.setLastName("1-lastName");
		user.setEmail("1-email");
		user.setCity(city);

		Vehicle vehicle1 = new Vehicle();
		Vehicle vehicle2 = new Vehicle();

		user = userBusinessService.create(user);
		System.out.println("User id: " + user.getId());
		
		User user2 = new User();
		user2.setId(user.getId());
		
		user2 = userBusinessService.addVehicle(user2, vehicle1);
		user2 = userBusinessService.addVehicle(user2, vehicle2);
		System.out.println(user2.getVehicles().size());
		
		user = userBusinessService.getChild(user.getId());
		System.out.println(user.getVehicles().size());


		
	}
}
