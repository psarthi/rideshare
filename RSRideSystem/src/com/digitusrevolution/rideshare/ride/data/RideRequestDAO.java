package com.digitusrevolution.rideshare.ride.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;

public class RideRequestDAO extends GenericDAOImpl<RideRequestEntity, Integer>{
	
	private static final Class<RideRequestEntity> entityClass = RideRequestEntity.class;;

	public RideRequestDAO() {
		super(RideRequestEntity.class);
	}
	
	/*
	 * Purpose: Get all valid ride request based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	public Set<RideRequestEntity> getValidRideRequests(Set<Integer> rideRequestIds){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		@SuppressWarnings("unchecked")
		List<RideRequestEntity> rideRequestEntities = criteria.add(Restrictions.in("id", rideRequestIds))
				.add(Restrictions.eq("status", "unfulfilled")).list();
		Set<RideRequestEntity> rideRequestEntitiesSet = new HashSet<>(rideRequestEntities);
		return rideRequestEntitiesSet;		
	}



}
