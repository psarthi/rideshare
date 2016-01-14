package com.digitusrevolution.rideshare.ride.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;

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


}
