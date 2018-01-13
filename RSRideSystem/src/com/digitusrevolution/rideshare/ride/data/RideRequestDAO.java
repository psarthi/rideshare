package com.digitusrevolution.rideshare.ride.data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
import com.digitusrevolution.rideshare.common.util.PropertyReader;
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
	public Set<RideRequestEntity> getValidRideRequests(Set<Integer> rideRequestIds, int availableSeats, RideMode createdRideMode, UserEntity driver){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideRequestEntity> rideRequestEntities = new HashSet<>(criteria.add(Restrictions.in("id", rideRequestIds))
				.add(Restrictions.eq("status", RideRequestStatus.Unfulfilled))
				.add(Restrictions.ne("passenger", driver))
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
	 * Commented this as we have found a better way to get the result set as shown in another function below
	 * Keeping it here for reference purpose and in case in future if required
	 * 
	 * Purpose - Get sublist of ride request for a user sorted by pickupTime
	 * 
	public List<RideRequestEntity> getRideRequests(UserEntity passenger, int startIndex, int endIndex){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		//LinkedHashSet will maintain the order of insertion and we will get proper sublist, so don't user HashSet
		Set<RideRequestEntity> rideRequestEntities = new LinkedHashSet<> (criteria.add(Restrictions.eq("passenger", passenger))
				.addOrder(Order.desc("pickupTime"))
				.list());
		
		logger.debug("Ride Request List Size:"+rideRequestEntities.size());		
		//This will take care of issue - java.lang.IndexOutOfBoundsException: toIndex = 10 when size is less than 10
		if (endIndex > rideRequestEntities.size()) endIndex = rideRequestEntities.size();
		//This is required to handle those scenario where you get request your startindex itself is more than the size e.g. on scroll of rides list it may send additional request
		if (startIndex > rideRequestEntities.size()) startIndex = rideRequestEntities.size();
		List<RideRequestEntity> rideRequestEntitiesList = new LinkedList<>(rideRequestEntities);
		//VERY IMP - Reason for not doing sublist directly there in list in criteria itself as it was having some weired behavior
		//and it was not returning proper list count and it was varying intermittently
		List<RideRequestEntity> rideRequestEntitiesSubList = rideRequestEntitiesList.subList(startIndex, endIndex);
		logger.debug("Ride Request Sub List Size:"+rideRequestEntitiesSubList.size());
		
		return rideRequestEntitiesSubList;		
	}
	 */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RideRequestEntity> getRideRequests(UserEntity passenger, int startIndex){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set rideRequestEntities = new HashSet<>(criteria.add(Restrictions.eq("passenger", passenger))
				.addOrder(Order.desc("pickupTime"))
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit)
				.list());
		
		logger.debug("Ride Request List Size:"+rideRequestEntities.size());		
		List<RideRequestEntity> rideRequestEntitiesList = new LinkedList<>(rideRequestEntities);
		return rideRequestEntitiesList;		
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
