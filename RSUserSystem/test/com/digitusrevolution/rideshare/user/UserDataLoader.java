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
import com.digitusrevolution.rideshare.user.domain.CityDO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

@Path("/domain/loadsample")
@Consumes(MediaType.APPLICATION_JSON)
public class UserDataLoader {
	
	
	private static final Logger logger = LogManager.getLogger(UserDataLoader.class.getName());
	
	@GET
	public Response load(){
		
		String[] args ={};
		UserDataLoader.main(args);
		return Response.ok().build();
	}
	
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			UserDataLoader dataLoader = new UserDataLoader();
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
		logger.entry();
		CityDO cityDO = new CityDO();
		City city = new City();
		city.setName("Bangalore");
		cityDO.create(city);
		city.setName("Chennai");
		cityDO.create(city);
		city.setName("Mumbai");
		cityDO.create(city);
		city.setName("New Delhi");
		cityDO.create(city);
		city.setName("Kolkata");
		cityDO.create(city);
		
	}

	public void loadRole(){
		logger.entry();		
		RoleDO roleDO = new RoleDO();
		Role role = new Role();
		role.setName("Passenger");
		roleDO.create(role);
		role.setName("Driver");
		roleDO.create(role);		
	
	}
	
	public void loadUser(){
		logger.entry();
		UserDO userDO = new UserDO();
		CityDO cityDO = new CityDO();
		RoleDO roleDO = new RoleDO();
		User user = new User();
		City city = new City();
		Role role = new Role();
		role = roleDO.get("Passenger");
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
			city = cityDO.get(i);
			user.setCity(city);
			userDO.update(user);

		}
		
	}
	
	public void loadVehicleCategory(){
		logger.entry();
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory.setName("Car");
		
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		vehicleCategoryDO.create(vehicleCategory);
	}
	
	public void loadVehicleSubCategory(){
		logger.entry();
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory.setName("Sedan");
		vehicleSubCategory.setAirConditioner(true);
		
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		vehicleSubCategoryDO.create(vehicleSubCategory);
		vehicleSubCategory = vehicleSubCategoryDO.get(1);
		
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();	
		VehicleCategory vehicleCategory = vehicleCategoryDO.get(1);
		
		vehicleCategory.getSubCategories().add(vehicleSubCategory);
		vehicleCategoryDO.update(vehicleCategory);
	}

		
	public void loadVehicle(){
		logger.entry(); 		
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		VehicleCategory vehicleCategory = vehicleCategoryDO.get(1);		
		
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		VehicleSubCategory vehicleSubCategory = vehicleSubCategoryDO.get(1);

		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleCategory(vehicleCategory);
		vehicle.setVehicleSubCategory(vehicleSubCategory);
		
		for (int i=1;i<2;i++){
			UserDO userDO = new UserDO();
			User user = userDO.getChild(i);
			userDO.setUser(user);			
			userDO.addVehicle(vehicle);
		}
		
	}

	
}
