package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;

public class VehicleCategoryDomainService implements DomainService<VehicleCategory>{

	private static final Logger logger = LogManager.getLogger(VehicleCategoryDomainService.class.getName());

	@Override
	public VehicleCategory get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		VehicleCategory vehicleCategory = null;
		try {
			transaction = session.beginTransaction();

			VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
			if (fetchChild){
				vehicleCategory = vehicleCategoryDO.getAllData(id);
			} else {
				vehicleCategory = vehicleCategoryDO.get(id);			
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
		return vehicleCategory;
	}

	@Override
	public List<VehicleCategory> getAll() {
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
