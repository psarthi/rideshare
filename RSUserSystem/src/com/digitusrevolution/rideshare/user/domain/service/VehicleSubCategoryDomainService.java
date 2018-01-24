package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainServiceInteger;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;

public class VehicleSubCategoryDomainService implements DomainServiceInteger<VehicleSubCategory>{

	private static final Logger logger = LogManager.getLogger(VehicleSubCategoryDomainService.class.getName());
	
	@Override
	public VehicleSubCategory get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		VehicleSubCategory vehicleSubCategory = null;
		try {
			transaction = session.beginTransaction();

			VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
			if (fetchChild){
				vehicleSubCategory = vehicleSubCategoryDO.getAllData(id);
			} else {
				vehicleSubCategory = vehicleSubCategoryDO.get(id);			
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
		return vehicleSubCategory;
	}

	@Override
	public List<VehicleSubCategory> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<VehicleSubCategory> vehicleSubCategories = null;
		try {
			transaction = session.beginTransaction();

			VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
			vehicleSubCategories = vehicleSubCategoryDO.getAll();

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
		return vehicleSubCategories;	
	}

}
