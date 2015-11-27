package com.digitusrevolution.rideshare.user;

import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;
import com.digitusrevolution.rideshare.user.business.facade.UserDomainFacade;

public class UserSystemTest {

	public static void main(String args[]){
		
		VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
		Vehicle vehicle = new Vehicle();
		Vehicle vehicle1 = new Vehicle();
		vehicleRegistrationService.addVehicle(20,vehicle);
		vehicleRegistrationService.addVehicle(20,vehicle1);

		
		/*
		UserDomainFacade userDomainFacade = new UserDomainFacade();
		User user = new User();
		City city = new City();
		user.setFirstName("firstName");
		user.setLastName("lastName");
		city.setName("name");
		user.setCity(city);
		int id = userDomainFacade.create(user);
		System.out.println("User Id: "+id);

		*/
		
		
		
		
		
	}
}
