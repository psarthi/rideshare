package com.digitusrevolution.rideshare.ride.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

public class RideDomainService implements DomainService<Ride>{

	private static final Logger logger = LogManager.getLogger(RideDomainService.class.getName());

	@Override
	public Ride get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Ride ride = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			if (fetchChild){
				ride = rideDO.getAllData(id);
			} else {
				ride = rideDO.get(id);			
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
		return ride;	
	}

	@Override
	public List<Ride> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Ride> rides = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rides = rideDO.getAll();			

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
		return rides;	
	}
}
