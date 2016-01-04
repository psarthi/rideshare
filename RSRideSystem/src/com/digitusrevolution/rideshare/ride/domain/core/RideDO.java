package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.ride.data.RideDAO;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;

public class RideDO implements DomainObjectPKInteger<Ride>{
	
	private static final Logger logger = LogManager.getLogger(RideDO.class.getName());
	private Ride ride;
	private RideEntity rideEntity;
	private RideMapper rideMapper;
	private final RideDAO rideDAO;
	private final RidePointDAO ridePointDAO;
	
	public RideDO() {
		ride = new Ride();
		rideEntity = new RideEntity();
		rideMapper = new RideMapper();
		rideDAO = new RideDAO();
		ridePointDAO = new RidePointDAO();
	}
	
	public void setRide(Ride ride) {
		this.ride = ride;
		rideEntity = rideMapper.getEntity(ride);
	}

	private void setRideEntity(RideEntity rideEntity) {
		this.rideEntity = rideEntity;
		ride = rideMapper.getDomainModel(rideEntity);
	}

	@Override
	public void fetchChild() {
		ride = rideMapper.getDomainModelChild(ride, rideEntity);
	}

	@Override
	public List<Ride> getAll() {
		List<Ride> rides = new ArrayList<>();
		List<RideEntity> rideEntities = rideDAO.getAll();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
			//Purposefully not getting routes as it would be too much data
			getRidePoints();
			rides.add(ride);
		}
		return rides;

	}

	//Update of RidePoints needs to be well thought - TBD
	@Override
	public void update(Ride ride) {
		if (ride.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+ride.getId());
		}
		setRide(ride);
		rideDAO.update(rideEntity);
	}

	//Delete of RidePoints needs to be well thought as it may involves multiple rides as well - TBD
	@Override
	public void delete(Ride ride) {
		setRide(ride);
		rideDAO.delete(rideEntity);
	}
	
	@Override
	public int create(Ride ride) {
		logger.entry();
		setRide(ride);
		int id = rideDAO.create(rideEntity);
		logger.exit();		
		return id;
	}

	@Override
	public Ride get(int id) {
		rideEntity = rideDAO.get(id);
		if (rideEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRideEntity(rideEntity);
		getRidePoints();
		return ride;
	}
	
	private void getRidePoints(){
		RidePoint startPoint = ridePointDAO.getRidePointOfRide(ride.getStartPoint().get_id(),ride.getId());
		RidePoint endPoint = ridePointDAO.getRidePointOfRide(ride.getEndPoint().get_id(), ride.getId());		
		ride.setStartPoint(startPoint);
		ride.setEndPoint(endPoint);			
	}

	@Override
	public Ride getChild(int id) {
		get(id);
		fetchChild();
		getRoute();
		return ride;
	}

	private void getRoute() {
		List<RidePoint> ridePoints = ridePointDAO.getAllRidePointsOfRide(ride.getId());
		Route route = new Route();
		route.setRidePoints(ridePoints);
		ride.setRoute(route);
	}

	/*
	 * Purpose - Goal of this function is to create ride as well as all the associated ride points in database
	 * 
	 * High Level Logic - 
	 * 
	 * - Check if the user has Driver role
	 * - if user is not driver, then ride can't be offered
	 * - if driver, then check for ride recurrence
	 * - if its recurring ride, generate multiple rides (TBD) 
	 * - Create all rides in database without start/end point 
	 * - Get all the ride point and generate the primary key for them for each rides (In case of recurring rides - TBD)
	 * - Update all the ride point in the database 
	 * - On success, update the ride with start point and end point primary key  
	 * 
	 * TBD -
	 *
	 * 1. Should we store starttime in Ride DB or it should be part of MongoDB startPoint
	 *    - Need to think on how to avoid multiple transaction between DBs for basic purpose as its two different DBs
	 * 2. What needs to be done when ride gets deleted in terms of its ride point or its updated
	 * 
	 */
	public int offerRide(Ride ride, GoogleDirection direction){
		int userId = ride.getDriver().getId();
		Collection<Role> roles = RESTClientUtil.getRoles(userId);
		int id = 0;
		boolean driverStatus = false;
		for (Role role : roles) {
			if (role.getName().equals("Driver")){
				driverStatus = true;
				ride.setStatus("planned");
				ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
				ride.setStartTime(startTimeUTC);				
				//Check if ride is recurring, then create multiple rides as per the recurring details
				//**TBD - Recurring code needs to be written later
				int[] ids = new int[5];
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<5; i++){
						ride.setStartTime(startTimeUTC.plusDays(i));
						ids[i] = create(ride);		
						logger.debug("Ride has been created with id:" + ids[i]);						
					}
				}
				
				if (!ride.getRecur()){
					id = create(ride);		
					logger.debug("Ride has been created with id:" + id);
				}
				
				RouteDO routeDO = new RouteDO();
				List<RideBasicInfo> ridesBasicInfo = new ArrayList<>();
				//In case its a recurring ride, then create multiple rides and add all of them below
				//**TBD - Recurring scenarios has to be written later
				if(ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<5; i++){
						RideBasicInfo rideBasicInfo = new RideBasicInfo();
						rideBasicInfo.setId(ids[i]);
						rideBasicInfo.setDateTime(startTimeUTC.plusDays(i));
						ridesBasicInfo.add(rideBasicInfo);
					}					
				}			
				if(!ride.getRecur()){
					RideBasicInfo rideBasicInfo = new RideBasicInfo();
					rideBasicInfo.setId(id);
					rideBasicInfo.setDateTime(startTimeUTC);
					ridesBasicInfo.add(rideBasicInfo);					
				}
				Route route = routeDO.getRoute(direction, ridesBasicInfo);
				Collection<RidePoint> ridePoints = route.getRidePoints();
				//Insert primary key here itself, so that we can update the start and end location in the ride table
				String startPointId;
				String endPointId = null;
				for (RidePoint ridePoint : ridePoints) {					
					ObjectId _id = new ObjectId();
					ridePoint.set_id(_id.toString());
					//This will keep overwriting the endPointId and last value would be the endPointId
					//This is done here to avoid reiterating the loop again just to get this id
					endPointId = _id.toString();
				}
				startPointId = ridePoints.iterator().next().get_id();
				ridePointDAO.createBulk(ridePoints);
				ride.getStartPoint().set_id(startPointId);
				ride.getEndPoint().set_id(endPointId);
				//Below is imp, else it won't be able to update the ride which has been just created
				if (!ride.getRecur()){
					ride.setId(id);
					update(ride);
					logger.debug("Ride has been updated with id:"+ride.getId());					
				}
				//**TBD - Recurring scenarios has to be written later
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i=0; i<5; i++){
						ride.setId(ids[i]);
						update(ride);
						logger.debug("Ride has been updated with id:"+ride.getId());											
					}
				}
			} 
		}
		if (!driverStatus) {
			throw new NotAuthorizedException("Can't offer ride unless you are a Driver");
		} else {
			return id;
		}
	}
	
	
	/*
	 * High level logic -
	 * 
	 * - Get all ride points and for each ride points, find out all the ride request point within a specific distance
	 * - Other option, would be create a geometry which is square across all points and pass a geometry collection to get all the ride request point within that
	 * - Once all ride request has been recieved, for each ride request point, check if specific ride is a valid ride by calling standard ride search function
	 * - Finally, you will get valid ride requests, then for each of them do further validation e.g. seat etc. 
	 * 
	 */
	public List<RideRequest> searchRideRequests(Ride ride){

		return null;
	}

	public List<Ride> getUpcomingRides(int userId){
		
		return null;
	}
	
}
