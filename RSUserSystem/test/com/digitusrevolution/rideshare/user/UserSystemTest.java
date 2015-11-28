package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;
import com.digitusrevolution.rideshare.user.business.dto.VehicleDTO;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());
	
	public static void main(String args[]){
		
		logger.info("Logger Testing");

		/*
		VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
		Vehicle vehicle = new Vehicle();
		Vehicle vehicle1 = new Vehicle();
		VehicleDTO vehicleDTO = new VehicleDTO();
		vehicleDTO.setUserId(20);
		vehicleDTO.setVehicle(vehicle);
		vehicleRegistrationService.addVehicle(vehicleDTO);
		vehicleDTO.setVehicle(vehicle1);
		vehicleRegistrationService.addVehicle(vehicleDTO);

		
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
