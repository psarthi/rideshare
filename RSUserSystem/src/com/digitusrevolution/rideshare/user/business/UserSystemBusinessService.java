package com.digitusrevolution.rideshare.user.business;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.Interest;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.dto.BasicInterest;
import com.digitusrevolution.rideshare.user.domain.CountryDO;
import com.digitusrevolution.rideshare.user.domain.InterestDO;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;

public class UserSystemBusinessService {

	private static final Logger logger = LogManager.getLogger(UserSystemBusinessService.class.getName());
	
	public List<Country> getCountries(){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Country> countries = null;
		try {
			transaction = session.beginTransaction();
			
			CountryDO countryDO = new CountryDO();
			countries = countryDO.getAll();
						
			transaction.commit();
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

		return countries;

	}

	public List<VehicleCategory> getVehicleCategories(){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<VehicleCategory> vehicleCategories = null;
		try {
			transaction = session.beginTransaction();
			
			VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
			vehicleCategories = vehicleCategoryDO.getAll();
						
			transaction.commit();
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
		return vehicleCategories;
	}
	
	public List<BasicInterest> getInterests(){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<BasicInterest> basicInterests = new LinkedList<>();
		try {
			transaction = session.beginTransaction();
			
			InterestDO interestDO = new InterestDO();
			List<Interest> interests = interestDO.getAll();
			//This will ensure we sort the list by name
			Collections.sort(interests);
			for (Interest interest: interests) {
				basicInterests.add(JsonObjectMapper.getMapper().convertValue(interest, BasicInterest.class));
			}
						
			transaction.commit();
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
		return basicInterests;
	}
}
