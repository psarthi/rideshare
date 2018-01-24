package com.digitusrevolution.rideshare.ride.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainServiceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainServiceLong;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideRequestDomainService implements DomainServiceLong<RideRequest>{

	private static final Logger logger = LogManager.getLogger(RideRequestDomainService.class.getName());

	@Override
	public RideRequest get(long id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		RideRequest rideRequest = null;
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			if (fetchChild){
				rideRequest = rideRequestDO.getAllData(id);
			} else {
				rideRequest = rideRequestDO.get(id);			
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
		return rideRequest;	
	}

	@Override
	public List<RideRequest> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<RideRequest> rideRequests = null;
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequests = rideRequestDO.getAll();			

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
		return rideRequests;	
	}
}
