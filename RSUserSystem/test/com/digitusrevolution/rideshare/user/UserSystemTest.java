package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.UserRegistrationService;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDO;
import com.digitusrevolution.rideshare.user.dto.UserAccount;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());
	
	public static void main(String args[]){
		
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
//			VehicleCategory vehicleCategory = new VehicleCategory();
//			vehicleCategory.setName("Car");
//			VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
//			//vehicleCategoryDO.create(vehicleCategory);
//			vehicleCategory = vehicleCategoryDO.get(1);
//			System.out.println(vehicleCategory.getName());
//			
//			VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
//			vehicleSubCategory.setName("Sedan");
//			vehicleSubCategory.setFuelType(FuelType.Petrol);
//			vehicleSubCategory.setAverageMileage(12);
//			VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
//			//vehicleSubCategoryDO.create(vehicleSubCategory);
//			vehicleSubCategory = vehicleSubCategoryDO.get(1);
			

			VehicleDO vehicleDO = new VehicleDO();
			Vehicle vehicle = vehicleDO.get(1);
//			vehicle.setVehicleCategory(vehicleCategory);
//			vehicle.setVehicleSubCategory(vehicleSubCategory);
			System.out.println(vehicle.getVehicleCategory().getSubCategories().size());
			System.out.println(vehicle.getVehicleSubCategory().getAverageMileage());
			System.out.println(vehicle.getVehicleCategory().getName());
			//vehicleDO.update(vehicle);
			
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
}
