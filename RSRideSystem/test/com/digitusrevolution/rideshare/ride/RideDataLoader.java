package com.digitusrevolution.rideshare.ride;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.TrustNetworkDO;


@Path("/domain/loaddata/ride")
public class RideDataLoader {
	
	private static final Logger logger = LogManager.getLogger(RideDataLoader.class.getName());
	
	@GET
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			RideDataLoader dataLoader = new RideDataLoader();

			//Trust Category needs to be loaded only once
			dataLoader.loadTrustCategory();
			dataLoader.loadTrustNetwork();
			
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
	
		public void loadTrustCategory(){
			TrustCategory trustCategory = new TrustCategory();
			trustCategory.setName(TrustCategoryName.Anonymous);

			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			trustCategoryDO.create(trustCategory);			
		}
		
		public void loadTrustNetwork(){
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anynomous");
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.addTrustCategory(trustCategory);
			TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
			trustNetworkDO.create(trustNetwork);
		}

		
}
