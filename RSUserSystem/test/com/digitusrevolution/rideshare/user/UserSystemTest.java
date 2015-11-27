package com.digitusrevolution.rideshare.user;

import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;
import com.digitusrevolution.rideshare.user.business.facade.UserDomainFacade;

public class UserSystemTest {

	public static void main(String args[]){

		UserDomainFacade userBusinessService = new UserDomainFacade();
		User user = new User();
		user = userBusinessService.get(20);
		userBusinessService.update(user);
		
	/*
		UserBusinessService userBusinessService = new UserBusinessService();
		User user = new User();
		City city = new City();
		user.setFirstName("firstName");
		user.setLastName("lastName");
		city.setName("name");
		user.setCity(city);
		int id = userBusinessService.create(user);
		System.out.println("User Id: "+id);
	
		VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
		Vehicle vehicle = new Vehicle();
		vehicleRegistrationService.addVehicle(20, vehicle);
	*/		
		
		
		
	}
}
