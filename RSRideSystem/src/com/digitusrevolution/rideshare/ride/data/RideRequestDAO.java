package com.digitusrevolution.rideshare.ride.data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
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
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class RideRequestDAO extends GenericDAOImpl<RideRequestEntity, Integer>{
	
	private static final Logger logger = LogManager.getLogger(RideRequestDAO.class.getName());
	private static final Class<RideRequestEntity> entityClass = RideRequestEntity.class;

	public RideRequestDAO() {
		super(RideRequestEntity.class);
	}
	
	/*
	 * Purpose: Get all valid ride request based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	public Set<RideRequestEntity> getValidRideRequests(Set<Integer> rideRequestIds, int availableSeats, RideMode createdRideMode){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideRequestEntity> rideRequestEntities = new HashSet<>(criteria.add(Restrictions.in("id", rideRequestIds))
				.add(Restrictions.eq("status", RideRequestStatus.Unfulfilled))
				.add(Restrictions.le("seatRequired", availableSeats)).list());
		
		//This will ensure we only get Paid Rides for paid Ride Request
		//For Free ride request, this is not required as Free and Paid both is fine
		//Use itereator to remove else you will get ConcurrentModification Exception
		if (createdRideMode.equals(RideMode.Paid)) {
			Iterator<RideRequestEntity> iterator = rideRequestEntities.iterator(); 
			while(iterator.hasNext()) {
				RideRequestEntity entity = iterator.next();
				if (!entity.getRideMode().equals(createdRideMode)) {
					iterator.remove();
					logger.debug("Removing ride request as its a free ride request and requirement is Paid: Ride Request Id:"+entity.getId());
				}
			}
		} 

		return rideRequestEntities;		
	}
	
	/*
	 * Purpose - Get current ride request
	 */
	public RideRequestEntity getCurrentRideRequest(UserEntity passenger){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
		RideRequestEntity rideRequestEntity = (RideRequestEntity) criteria.add(Restrictions.eq("passenger", passenger))
		.add(Restrictions.ge("pickupTime", currentTime))
		.add(Restrictions.or(Restrictions.ne("status", RideRequestStatus.Cancelled)))
		.add(Restrictions.ne("passengerStatus", PassengerStatus.Dropped))
		.addOrder(Order.asc("pickupTime"))
		.setMaxResults(1).uniqueResult();
		
		return rideRequestEntity;		
	}
	
	/*
	 * Purpose - Get all ride request of a user
	 */
	public Set<RideRequestEntity> getAllRideRequests(UserEntity passenger){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideRequestEntity> rideRequestEntities = new HashSet<> (criteria.add(Restrictions.eq("passenger", passenger))
				.list());
		return rideRequestEntities;		
	}

	

	/*
	 * Purpose - Get the status of ride request, this is required many times, so instead of using get and then fetching the status
	 * 			 this function would directly return the status
	 */
	public RideRequestStatus getStatus(int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		RideRequestStatus status = (RideRequestStatus) criteria.add(Restrictions.eq("id",rideRequestId))
				.setProjection(Projections.property("status")).uniqueResult();
		return status;
	}


}
