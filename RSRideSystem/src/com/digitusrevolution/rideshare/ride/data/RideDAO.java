package com.digitusrevolution.rideshare.ride.data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

public class RideDAO extends GenericDAOImpl<RideEntity, Integer>{

	private static final Logger logger = LogManager.getLogger(RideDAO.class.getName());
	private static final Class<RideEntity> entityClass = RideEntity.class;

	public RideDAO() {
		super(entityClass);	
	}

	/*
	 * Purpose: Get all valid rides based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	public Set<RideEntity> getValidRides(Set<Integer> rideIds, int seatRequired, RideMode rideRequestMode){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideEntity> rideEntities = new HashSet<>(criteria.add(Restrictions.in("id", rideIds))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned), Restrictions.eq("status", RideStatus.Started)))
		.add(Restrictions.eq("seatStatus", RideSeatStatus.Available))
		.add(Restrictions.ge("seatOffered", seatRequired)).list());
		
		//We will get Free Rides for Free Ride Request and get Free/Paid Rides for Paid Ride Request
		//Use itereator to remove else you will get ConcurrentModification Exception
		if (rideRequestMode.equals(RideMode.Free)) {
			Iterator<RideEntity> iterator = rideEntities.iterator(); 
			while(iterator.hasNext()) {
				RideEntity entity = iterator.next();
				if (!entity.getRideMode().equals(rideRequestMode)) {
					iterator.remove();
					logger.debug("Removing ride as its a Paid ride and requirement is Free: Ride Id:"+entity.getId());
				}
			}
		}
		
		return rideEntities;		
	}

	/*
	 * Purpose - Get all upcoming rides i.e. rides having start time greater than or equal to current time in UTC
	 * 			 Result should be limited to the required count  
	 */
	public Set<RideEntity> getAllUpcomingRides(UserEntity driver, int limit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideEntity> rideEntities = new HashSet<> (criteria.add(Restrictions.eq("driver", driver))
		.add(Restrictions.ge("startTime", currentTime))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned)))
		.setMaxResults(limit).list());		

		return rideEntities;	
	}
	
	/*
	 * Purpose - Get all rides of a user
	 */
	public Set<RideEntity> getAllRides(UserEntity driver){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideEntity> rideEntities = new HashSet<>(criteria.add(Restrictions.eq("driver", driver)).list());		

		return rideEntities;	
	}

	
	/*
	 * Purpose - Get current ride
	 */
	public RideEntity getCurrentRide(UserEntity driver){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		RideEntity rideEntity = (RideEntity) criteria.add(Restrictions.eq("driver", driver))
		.add(Restrictions.or(Restrictions.ge("startTime", currentTime),Restrictions.ge("endTime", currentTime)))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned),Restrictions.eq("status", RideStatus.Started),Restrictions.eq("status", RideStatus.Fulfilled)))
		.addOrder(Order.asc("startTime"))
		.setMaxResults(1).uniqueResult();	 

		return rideEntity;
	}


	/*
	 * Purpose - Get the status of ride, this is required many times, so instead of using get and then fetching the status
	 * 			 this function would directly return the status
	 */
	public RideStatus getStatus(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		RideStatus status = (RideStatus) criteria.add(Restrictions.eq("id",rideId))
				.setProjection(Projections.property("status")).uniqueResult();
		return status;
	}

}








