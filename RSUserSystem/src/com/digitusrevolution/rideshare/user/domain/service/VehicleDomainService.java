package com.digitusrevolution.rideshare.user.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDO;

public class VehicleDomainService implements DomainService<Vehicle>{
	
	private static final Logger logger = LogManager.getLogger(VehicleDomainService.class.getName());

	@Override
	public Vehicle get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		Vehicle vehicle = null;
		try {
			transation = session.beginTransaction();
	
			VehicleDO vehicleDO = new VehicleDO();
			if (fetchChild){
				vehicle = vehicleDO.getChild(id);
			} else {
				vehicle = vehicleDO.get(id);			
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
		return vehicle;
	}

	@Override
	public List<Vehicle> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<Vehicle> vehicles = null;
		try {
			transation = session.beginTransaction();

			VehicleDO vehicleDO = new VehicleDO();
			vehicles = vehicleDO.getAll();

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
		return vehicles;
	}
}
