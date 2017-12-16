package com.digitusrevolution.rideshare.ride.data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class RideDAO extends GenericDAOImpl<RideEntity, Integer>{

	private static final Class<RideEntity> entityClass = RideEntity.class;

	public RideDAO() {
		super(entityClass);	
	}

	/*
	 * Purpose: Get all valid rides based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	public Set<RideEntity> getValidRides(Set<Integer> rideIds, int seatRequired){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.in("id", rideIds))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned))
				.add(Restrictions.eq("status", RideStatus.Started)))
		.add(Restrictions.eq("seatStatus", RideSeatStatus.Available))
		.add(Restrictions.ge("seatOffered", seatRequired)).list();

		Set<RideEntity> rideEntitiesSet = new HashSet<>(rideEntities);
		return rideEntitiesSet;		
	}

	/*
	 * Purpose - Get all upcoming rides i.e. rides having start time greater than or equal to current time in UTC
	 * 			 Result should be limited to the required count  
	 */
	public List<RideEntity> getAllUpcomingRides(UserEntity driver, int limit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.eq("driver", driver))
		.add(Restrictions.ge("startTime", currentTime))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned)))
		.setMaxResults(limit).list();		

		return rideEntities;	
	}
	
	/*
	 * Purpose - Get all rides of a user
	 */
	public List<RideEntity> getAllRides(UserEntity driver){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.eq("driver", driver)).list();		

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
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned),Restrictions.eq("status", RideStatus.Started)))
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








