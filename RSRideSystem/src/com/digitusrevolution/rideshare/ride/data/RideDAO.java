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
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

public class RideDAO extends GenericDAOImpl<RideEntity, Long>{

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
	public Set<RideEntity> getValidRides(Set<Long> rideIds, int seatRequired, RideMode rideRequestMode, UserEntity passenger){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideEntity> rideEntities = new HashSet<>(criteria.add(Restrictions.in("id", rideIds))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned), Restrictions.eq("status", RideStatus.Started)))
		.add(Restrictions.eq("seatStatus", RideSeatStatus.Available))
		.add(Restrictions.ne("driver", passenger))
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
		ZonedDateTime currentTime = DateTimeUtil.getCurrentTimeInUTC();
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		Set<RideEntity> rideEntities = new HashSet<> (criteria.add(Restrictions.eq("driver", driver))
		.add(Restrictions.ge("startTime", currentTime))
		.add(Restrictions.or(Restrictions.eq("status", RideStatus.Planned)))
		.setMaxResults(limit).list());		

		return rideEntities;	
	}
	
	/*
	 * 
	 * Commented this as we have found a better way to get the result set as shown in another function below
	 * Keeping it here for reference purpose and in case in future if required
	 * 
	 * Purpose - Get sublist of rides for a user sorted by startTime
	 *
	 *
	public List<RideEntity> getRides(UserEntity driver, int startIndex, int endIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		//VERY IMP - Get the result in Set else you would get duplicate values
		@SuppressWarnings("unchecked")
		//LinkedHashSet will maintain the order of insertion and we will get proper sublist, so don't user HashSet
		Set<RideEntity> rideEntities = new LinkedHashSet<>(criteria.add(Restrictions.eq("driver", driver))
				.addOrder(Order.desc("startTime"))
				.list());		

		logger.debug("Ride List Size:"+rideEntities.size());
		//This will take care of issue - java.lang.IndexOutOfBoundsException: toIndex = 10 when size is less than 10
		if (endIndex > rideEntities.size()) endIndex = rideEntities.size();
		//This is required to handle those scenario where you get request your startindex itself is more than the size e.g. on scroll of rides list it may send additional request
		if (startIndex > rideEntities.size()) startIndex = rideEntities.size();
		List<RideEntity> rideEntitiesList = new LinkedList<>(rideEntities);
		//VERY IMP - Reason for not doing sublist directly there in list in criteria itself as it was having some weired behavior
		//and it was not returning proper list count and it was varying intermittently
		List<RideEntity> rideEntitiesSubList = rideEntitiesList.subList(startIndex, endIndex);
		logger.debug("Ride Sub List Size:"+rideEntitiesSubList.size());

		
		return rideEntitiesSubList;	
	}
	*/
	
	/*
	 * Purpose - Get list of rides based on startindex with max result count to support pagination
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RideEntity> getRides(UserEntity driver, int startIndex){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		int resultLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("MAX_RESULT_LIMIT"));
		//VERY IMP - Get the result in Set else you would get duplicate values
		Set rideEntities = new HashSet<>(criteria.add(Restrictions.eq("driver", driver))
				.addOrder(Order.desc("startTime"))
				.setFirstResult(startIndex)
				.setMaxResults(resultLimit)
				.list());		
		
		logger.debug("Ride List Size:"+rideEntities.size());
		List<RideEntity> rideEntitiesList = new LinkedList<>(rideEntities);
		return rideEntitiesList;	
	}

	
	/*
	 * Purpose - Get current ride
	 */
	public RideEntity getCurrentRide(UserEntity driver){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		ZonedDateTime currentTime = DateTimeUtil.getCurrentTimeInUTC();
		//VERY IMP - Don't add any extra buffer in the current time so that you can show current ride for some extra time than the end time
		//what would happen with that, if you add buffer in the current time then your comparision would be with extra and you may not even 
		//get any current ride as your start time and end time may be before the current time + buffer time
		//If you compare with current time, benefit is even if start time is same as current time with few seconds difference 
		//but your end time would atleast be ahead of your current time due to travel time
		//so you will always get current ride post creation of ride
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
	public RideStatus getStatus(long rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(entityClass);
		RideStatus status = (RideStatus) criteria.add(Restrictions.eq("id",rideId))
				.setProjection(Projections.property("status")).uniqueResult();
		return status;
	}

}








