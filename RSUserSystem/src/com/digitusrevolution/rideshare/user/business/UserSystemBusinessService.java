package com.digitusrevolution.rideshare.user.business;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.user.domain.CountryDO;
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
}
