package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;

public class VehicleCategoryDomainService implements DomainService<VehicleCategory>{

	private static final Logger logger = LogManager.getLogger(VehicleCategoryDomainService.class.getName());

	@Override
	public VehicleCategory get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		VehicleCategory vehicleCategory = null;
		try {
			transation = session.beginTransaction();

			VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
			if (fetchChild){
				vehicleCategory = vehicleCategoryDO.getChild(id);
			} else {
				vehicleCategory = vehicleCategoryDO.get(id);			
			}

			transation.commit();
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
		return vehicleCategory;
	}

	@Override
	public List<VehicleCategory> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<VehicleCategory> vehicleCategories = null;
		try {
			transation = session.beginTransaction();

			VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
			vehicleCategories = vehicleCategoryDO.getAll();

			transation.commit();
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
		return vehicleCategories;	
	}


}
