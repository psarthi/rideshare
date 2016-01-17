package com.digitusrevolution.rideshare.ride.data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
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
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.in("id", rideIds))
												.add(Restrictions.eq("status", "planned")).list();

		Set<RideEntity> rideEntitiesSet = new HashSet<>(rideEntities);
		return rideEntitiesSet;		
	}

	public List<RideEntity> getUpcomingRides(UserEntity driver, int limit){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		@SuppressWarnings("unchecked")
		List<RideEntity> rideEntities = criteria.add(Restrictions.eq("driver", driver))
												.add(Restrictions.ge("startTime", currentTime))
												.add(Restrictions.or(Restrictions.eq("status", "planned"),
																	 Restrictions.eq("status", "fulfilled")))
												.setMaxResults(limit).list();		
		
		return rideEntities;	
	}

}
