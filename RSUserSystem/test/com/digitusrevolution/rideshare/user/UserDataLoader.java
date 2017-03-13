package com.digitusrevolution.rideshare.user;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.RoleName;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.State;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;
import com.digitusrevolution.rideshare.user.domain.CityDO;
import com.digitusrevolution.rideshare.user.domain.CountryDO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;
import com.digitusrevolution.rideshare.user.domain.StateDO;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

@Path("/domain/loaddata/user")
public class UserDataLoader {
	
	
	private static final Logger logger = LogManager.getLogger(UserDataLoader.class.getName());
	
	@GET
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			UserDataLoader dataLoader = new UserDataLoader();
	
			dataLoader.loadCountry();
			dataLoader.loadRole();
			dataLoader.loadVehicleCategory();
			dataLoader.loadVehicleSubCategory();
			
			dataLoader.loadUser();
			dataLoader.loadVehicle();
			dataLoader.addAccount();
			
			
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
	
	public void loadCountry(){

		Currency currency = new Currency();
		currency.setName("INR");
		currency.setConversionRate(1);
		
		Country country = new Country();
		country.setCurrency(currency);
		country.setName("India");
		
		Fuel fuel = new Fuel();
		fuel.setType(FuelType.Petrol);
		fuel.setPrice(60);

		country.getFuels().add(fuel);
		
		State state = new State();
		state.setName("Karnataka");
		
		City city = new City();
		city.setName("Bangalore");
		state.getCities().add(city);

		country.getStates().add(state);
		
		CountryDO countryDO = new CountryDO();
		countryDO.create(country);
		
	}
	
	public void loadRole(){
		logger.entry();		
		RoleDO roleDO = new RoleDO();
		Role role = new Role();
		role.setName(RoleName.Passenger);
		roleDO.create(role);
		role.setName(RoleName.Driver);
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
		role = roleDO.get(RoleName.Passenger.toString());
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
			CountryDO countryDO = new CountryDO();
			Country country = countryDO.get("India");
			city = cityDO.get(1);
			StateDO stateDO = new StateDO();
			State state = stateDO.get(1);
			user.setCity(city);
			user.setState(state);
			user.setCountry(country);
			
			userDO.create(user);

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
		vehicleSubCategory.setFuelType(FuelType.Petrol);
		vehicleSubCategory.setAverageMileage(12);
		
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
		
		for (int i=1;i<3;i++){
			UserDO userDO = new UserDO();
			User user = userDO.getAllData(i);
			userDO.setUser(user);			
			userDO.addVehicle(vehicle);
		}	
	}
	
	public void addAccount(){
		UserDO userDO = new UserDO();
		for (int i=1; i<6; i++){
			Account account = RESTClientUtil.getAccount(i);
			userDO.addAccount(i, account);			
		}

	}

	
}
