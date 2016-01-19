package com.digitusrevolution.rideshare.ride.data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class RideDAO extends GenericDAOImpl<RideEntity, Integer>{

	private static final Class<RideEntity> entityClass = RideEntity.class;;

	public RideDAO() {
		super(entityClass);	
	}

	/*
	 * Purpose: Get all valid rides based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	public Set<RideEntity> getValidRides(Set<Integer> rideIds){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		String initialStatus = PropertyReader.getInstance().getProperty("RIDE_INITIAL_STATUS");
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.in("id", rideIds))
		.add(Restrictions.eq("status", initialStatus)).list();

		Set<RideEntity> rideEntitiesSet = new HashSet<>(rideEntities);
		return rideEntitiesSet;		
	}

	/*
	 * Purpose - Get all upcoming rides i.e. rides having start time greater than or equal to current time in UTC
	 * 			 Result should be limited to the required count 
	 * 
	 */
	public List<RideEntity> getUpcomingRides(UserEntity driver, int limit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		String initialStatus = PropertyReader.getInstance().getProperty("RIDE_INITIAL_STATUS");
		String fulfilledStatus = PropertyReader.getInstance().getProperty("RIDE_FULFILLED_STATUS");
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.eq("driver", driver))
		.add(Restrictions.ge("startTime", currentTime))
		.add(Restrictions.or(Restrictions.eq("status", initialStatus),
				Restrictions.eq("status", fulfilledStatus)))
		.setMaxResults(limit).list();		

		return rideEntities;	
	}

	/*
	 * Purpose - Get the status of ride, this is required many times, so instead of using get and then fetching the status
	 * 			 this function would directly return the status
	 */
	public String getStatus(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		String status = (String) criteria.add(Restrictions.eq("id",rideId))
				.setProjection(Projections.property("status")).uniqueResult();
		return status;
	}

}








