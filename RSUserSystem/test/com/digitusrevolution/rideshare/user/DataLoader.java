package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.CityDomainService;
import com.digitusrevolution.rideshare.user.domain.RoleDomainService;
import com.digitusrevolution.rideshare.user.domain.core.UserDomainService;

public class DataLoader {
	
	
	private static final Logger logger = LogManager.getLogger(DataLoader.class.getName());
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			DataLoader dataLoader = new DataLoader();
			dataLoader.loadCity();
			dataLoader.loadRole();
			dataLoader.loadUser();
			
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
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
		User user = new User();
		City city = new City();
	
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

	
}
