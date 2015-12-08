package com.digitusrevolution.rideshare.ride.domain.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideRequestDomainService implements DomainService<RideRequest>{

	private static final Logger logger = LogManager.getLogger(RideRequestDomainService.class.getName());

	@Override
	public RideRequest get(int id, boolean fetchChild) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		RideRequest rideRequest = null;
		try {
			transation = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			if (fetchChild){
				rideRequest = rideRequestDO.getChild(id);
			} else {
				rideRequest = rideRequestDO.get(id);			
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
		return rideRequest;	
	}

	@Override
	public List<RideRequest> getAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<RideRequest> rideRequests = null;
		try {
			transation = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequests = rideRequestDO.getAll();			

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
		return rideRequests;	
	}

}
