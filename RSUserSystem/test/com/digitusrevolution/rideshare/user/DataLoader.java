package com.digitusrevolution.rideshare.user;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.CityDomainService;
import com.digitusrevolution.rideshare.user.domain.RoleDomainService;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDomainService;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDomainService;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDomainService;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDomainService;

@Path("/domain/loadsample")
@Consumes(MediaType.APPLICATION_JSON)
public class DataLoader {
	
	
	private static final Logger logger = LogManager.getLogger(DataLoader.class.getName());
	
	@GET
	public Response load(){
		
		String[] args ={};
		DataLoader.main(args);
		return Response.ok().build();
	}
	
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			DataLoader dataLoader = new DataLoader();
			dataLoader.loadCity();
			dataLoader.loadRole();
			dataLoader.loadUser();
			dataLoader.loadVehicleCategory();
			dataLoader.loadVehicleSubCategory();
			dataLoader.loadVehicle();			
			
			transation.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}	

	}
	
	public void loadCity(){
		CityDomainService cityDomainService = new CityDomainService();
		City city = new City();
		city.setName("Bangalore");
		cityDomainService.create(city);
		city.setName("Chennai");
		cityDomainService.create(city);
		city.setName("Mumbai");
		cityDomainService.create(city);
		city.setName("New Delhi");
		cityDomainService.create(city);
		city.setName("Kolkata");
		cityDomainService.create(city);
		
	}

	public void loadRole(){
		
		RoleDomainService roleDomainService = new RoleDomainService();
		Role role = new Role();
		role.setName("Passenger");
		roleDomainService.create(role);
		role.setName("Driver");
		roleDomainService.create(role);		
	
	}
	
	public void loadUser(){
		
		UserDomainService userDomainService = new UserDomainService();
		CityDomainService cityDomainService = new CityDomainService();
		RoleDomainService roleDomainService = new RoleDomainService();
		User user = new User();
		City city = new City();
		Role role = new Role();
		role = roleDomainService.get("Passenger");
		user.getRoles().add(role);

	
		for (int i=1; i<6; i++){
	
			user.setFirstName("firstName-"+i);
			user.setLastName("lastName-"+i);
			user.setEmail("email-"+i);
			user.setMobileNumber("mobileNumber-"+i);
			if ((i & 1)==0){
				user.setSex(Sex.Male);				
			}else {
				user.setSex(Sex.Female);
			}			
			city = cityDomainService.get(i);
			user.setCity(city);
			userDomainService.update(user);

		}
		
	}
	
	public void loadVehicleCategory(){
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory.setName("Car");
		VehicleCategoryDomainService vehicleCategoryDomainService = new VehicleCategoryDomainService();
		vehicleCategoryDomainService.create(vehicleCategory);
	}
	
	public void loadVehicleSubCategory(){
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory.setName("Sedan");
		vehicleSubCategory.setAirConditioner(true);
		VehicleSubCategoryDomainService vehicleSubCategoryDomainService = new VehicleSubCategoryDomainService();
		vehicleSubCategoryDomainService.create(vehicleSubCategory);
		
		VehicleCategoryDomainService vehicleCategoryDomainService = new VehicleCategoryDomainService();
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory = vehicleCategoryDomainService.get(1);
		vehicleSubCategory = vehicleSubCategoryDomainService.get(1);
		
		vehicleCategory.getSubCategories().add(vehicleSubCategory);
		vehicleCategoryDomainService.update(vehicleCategory);
	}

		
	public void loadVehicle(){
		
		UserDomainService userDomainService = new UserDomainService();
		VehicleDomainService vehicleDomainService = new VehicleDomainService();
		UserDO userDO = new UserDO();
		User user = new User();
		Vehicle vehicle = new Vehicle();
		RoleDomainService roleDomainService = new RoleDomainService();
		Role role = roleDomainService.get("Driver");
		VehicleCategory vehicleCategory = new VehicleCategory();
		VehicleCategoryDomainService vehicleCategoryDomainService = new VehicleCategoryDomainService();
		vehicleCategory = vehicleCategoryDomainService.get(1);
		
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		VehicleSubCategoryDomainService vehicleSubCategoryDomainService = new VehicleSubCategoryDomainService();
		vehicleSubCategory = vehicleSubCategoryDomainService.get(1);
		
		vehicle.setVehicleCategory(vehicleCategory);
		vehicle.setVehicleSubCategory(vehicleSubCategory);
		
		Vehicle vehicle2 = new Vehicle();
		vehicle2.setVehicleCategory(vehicleCategory);
		vehicle2.setVehicleSubCategory(vehicleSubCategory);
		
		for (int i=0;i<2;i++){
			int id = vehicleDomainService.create(vehicle);
			vehicle = vehicleDomainService.get(id);
			user = userDomainService.getChild(1);
			if (user.getVehicles().size()==0){
				user.getRoles().add(role);
			}
			userDO.setUser(user);
			userDO.addVehicle(vehicle);
		}
		
		for (int i=0;i<2;i++){
			user = userDomainService.getChild(2);
			if (user.getVehicles().size()==0){
				user.getRoles().add(role);
			}
			userDO.setUser(user);			
			userDO.addVehicle(vehicle2);
		}
		
	}

	
}
