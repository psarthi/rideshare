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
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.Interest;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
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
import com.digitusrevolution.rideshare.user.domain.InterestDO;
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
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			
			UserDataLoader dataLoader = new UserDataLoader();
	
			//dataLoader.loadCountry();
			//dataLoader.loadRole();
			//dataLoader.loadVehicleCategory();
			//dataLoader.loadVehicleSubCategory();
			dataLoader.loadInterest();
			
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
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
		country.setCode("+91");
		country.setRideMode(RideMode.Free);
		
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
		
		VehicleSubCategory vehicleSubCategory1 = new VehicleSubCategory();
		vehicleSubCategory1.setName("All");
		vehicleSubCategory1.setAirConditioner(true);
		vehicleSubCategory1.setFuelType(FuelType.Petrol);
		vehicleSubCategory1.setAverageMileage(12);

		VehicleSubCategory vehicleSubCategory2 = new VehicleSubCategory();
		vehicleSubCategory2.setName("Hatchback");
		vehicleSubCategory2.setAirConditioner(true);
		vehicleSubCategory2.setFuelType(FuelType.Petrol);
		vehicleSubCategory2.setAverageMileage(12);
		
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		int id = vehicleSubCategoryDO.create(vehicleSubCategory);
		vehicleSubCategory = vehicleSubCategoryDO.get(id);
		
		id = vehicleSubCategoryDO.create(vehicleSubCategory1);
		vehicleSubCategory1 = vehicleSubCategoryDO.get(id);
		
		id = vehicleSubCategoryDO.create(vehicleSubCategory2);
		vehicleSubCategory2 = vehicleSubCategoryDO.get(id);
		
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();	
		VehicleCategory vehicleCategory = vehicleCategoryDO.get(1);
		
		vehicleCategory.getSubCategories().add(vehicleSubCategory);
		vehicleCategory.getSubCategories().add(vehicleSubCategory1);
		vehicleCategory.getSubCategories().add(vehicleSubCategory2);
		vehicleCategoryDO.update(vehicleCategory);
	}
	
	public void loadInterest() {
		
		String[] interests = {"Cooking", "Startup", "Reading", "Singing", "Painting", "Trekking", "Instruments", "Dancing", 
				"Photography", "Running", "Badminton", "Cycling", "Yoga", "Football", "Cricket"};
		String[] photo_name = {"cooking", "startup", "reading", "singing", "painting", "trekking", "musical_intstruments", "dancing", 
				"photography", "running", "badminton", "cycling", "yoga", "football", "cricket"};

		String awsRootUrl = PropertyReader.getInstance().getProperty("AWS_S3_ROOT_URL");
		String bucketName = PropertyReader.getInstance().getProperty("GROUP_PHOTO_BUCKET_NAME");
		String interestFolder = "interest";
		
		InterestDO interestDO = new InterestDO();

		for (int i =0; i< interests.length; i++) {
	        String fullUrl = awsRootUrl + "/" + bucketName + "/" + interestFolder + "/" + photo_name[i] +".jpg";
			System.out.println("Interest Name: "+interests[i]+" / "+ photo_name[i]);
	        System.out.println("File Path:"+fullUrl);
	        Interest interest = new Interest();
	        interest.setName(interests[i]);
	        Photo photo = new Photo();
	        photo.setImageLocation(fullUrl);
	        interest.setPhoto(photo);
	        interestDO.create(interest);
		}

	}
}







