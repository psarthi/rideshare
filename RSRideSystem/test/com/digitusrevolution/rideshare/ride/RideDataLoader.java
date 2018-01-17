package com.digitusrevolution.rideshare.ride;

import java.time.ZonedDateTime;

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
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;


@Path("/domain/loaddata/ride")
public class RideDataLoader {
	
	private static final Logger logger = LogManager.getLogger(RideDataLoader.class.getName());
	
	@GET
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			
			RideDataLoader dataLoader = new RideDataLoader();

			//Trust Category needs to be loaded only once
			dataLoader.loadTrustCategory();
			
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
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
	}
	
		public void loadTrustCategory(){
			TrustCategory trustCategory = new TrustCategory();
			trustCategory.setName(TrustCategoryName.Anonymous);

			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			trustCategoryDO.create(trustCategory);	
			trustCategory.setName(TrustCategoryName.Groups);
			trustCategoryDO.create(trustCategory);	
		}		
}
