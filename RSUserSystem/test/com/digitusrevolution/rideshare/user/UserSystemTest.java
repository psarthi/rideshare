package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;
import com.digitusrevolution.rideshare.user.business.facade.UserDomainFacade;
import com.digitusrevolution.rideshare.user.business.facade.resource.UserDomainResource;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());
	
	public static void main(String args[]){
		
		logger.info("Logger Testing");
		
		DataLoader dataLoader = new DataLoader();
		dataLoader.load();

//		UserDomainFacade domainFacade = new UserDomainFacade();
//		domainFacade.getRoles(1);
		
		UserDomainResource userDomainResource = new UserDomainResource();
		userDomainResource.getRoles(1);
		
		/*	

		VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
		Vehicle vehicle = new Vehicle();
		vehicleRegistrationService.addVehicle(1, vehicle);
	
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
