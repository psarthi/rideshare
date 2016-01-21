package com.digitusrevolution.rideshare.user;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.CityDO;
import com.digitusrevolution.rideshare.user.domain.CurrencyDO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDO;

@Path("/domain/loadsample/user")
public class UserDataLoader {
	
	
	private static final Logger logger = LogManager.getLogger(UserDataLoader.class.getName());
	
	@GET
	public Response load(){
		
		String[] args ={};
		UserDataLoader.main(args);
		return Response.ok().build();
	}
	
	@GET
	@Path("/prereq")
	public Response prereq(){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			UserDataLoader dataLoader = new UserDataLoader();
			dataLoader.loadCity();
			dataLoader.loadRole();
			dataLoader.loadVehicleCategory();
			dataLoader.loadVehicleSubCategory();
			
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
		
		return Response.ok().build();
	}
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			UserDataLoader dataLoader = new UserDataLoader();
			
			// This will take care of prerequisites
			//Start
//			dataLoader.loadCity();
//			dataLoader.loadRole();
//			dataLoader.loadVehicleCategory();
//			dataLoader.loadVehicleSubCategory();
			//End			
			
//			dataLoader.loadUser();
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
		CurrencyDO currencyDO = new CurrencyDO();
		Currency currency = new Currency();
		currency.setName("INR");
		currency.setConversionRate(1);
		
		
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
		String driverRole = PropertyReader.getInstance().getProperty("DRIVER_ROLE");
		String passengerRole = PropertyReader.getInstance().getProperty("PASSENGER_ROLE");
		role.setName(passengerRole);
		roleDO.create(role);
		role.setName(driverRole);
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
		String passengerRole = PropertyReader.getInstance().getProperty("PASSENGER_ROLE");
		role = roleDO.get(passengerRole);
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
			userDO.create(user);

		}
		
	}
	
	public void loadVehicleCategory(){
		logger.entry();
		VehicleCategory vehicleCategory = new VehicleCategory();
		String carCategory = PropertyReader.getInstance().getProperty("CAR_CATEGORY");
		vehicleCategory.setName(carCategory);
		
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		vehicleCategoryDO.create(vehicleCategory);
	}
	
	public void loadVehicleSubCategory(){
		logger.entry();
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		String carSubCategorySedan = PropertyReader.getInstance().getProperty("CAR_SUB_CATEGORY_SEDAN");
		vehicleSubCategory.setName(carSubCategorySedan);
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
		
		for (int i=2;i<3;i++){
			UserDO userDO = new UserDO();
			User user = userDO.getChild(i);
			userDO.setUser(user);			
			userDO.addVehicle(vehicle);
		}
		
	}

	
}
