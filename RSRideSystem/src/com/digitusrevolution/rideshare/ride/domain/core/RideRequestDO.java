package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDistance;

public class RideRequestDO implements DomainObjectPKInteger<RideRequest>{
	
	private static final Logger logger = LogManager.getLogger(RideRequestDO.class.getName());
	private RideRequest rideRequest;
	private RideRequestEntity rideRequestEntity;
	private RideRequestMapper rideRequestMapper;
	private final RideRequestDAO rideRequestDAO;
	private final RideRequestPointDAO rideRequestPointDAO;
	
	public RideRequestDO() {
		rideRequest = new RideRequest();
		rideRequestEntity = new RideRequestEntity();
		rideRequestMapper = new RideRequestMapper();
		rideRequestDAO = new RideRequestDAO();
		rideRequestPointDAO = new RideRequestPointDAO();
	}

	public void setRideRequest(RideRequest rideRequest) {
		this.rideRequest = rideRequest;
		rideRequestEntity = rideRequestMapper.getEntity(rideRequest);
	}

	private void setRideRequestEntity(RideRequestEntity rideRequestEntity) {
		this.rideRequestEntity = rideRequestEntity;
		rideRequest = rideRequestMapper.getDomainModel(rideRequestEntity);
	}

	@Override
	public void fetchChild() {
		rideRequest = rideRequestMapper.getDomainModelChild(rideRequest, rideRequestEntity);
		
	}
	
	@Override
	public List<RideRequest> getAll() {
		List<RideRequest> rideRequests = new ArrayList<>();
		List<RideRequestEntity> rideRequestEntities = rideRequestDAO.getAll();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			setRideRequestEntity(rideRequestEntity);
			getRideRequestPoint();
			rideRequests.add(rideRequest);
		}
		return rideRequests;
	}

	@Override
	public void update(RideRequest rideRequest) {
		if (rideRequest.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+rideRequest.getId());
		}
		setRideRequest(rideRequest);
		rideRequestDAO.update(rideRequestEntity);
	}

	@Override
	public void delete(RideRequest model) {
		setRideRequest(rideRequest);
		rideRequestDAO.delete(rideRequestEntity);
	}

	@Override
	public int create(RideRequest rideRequest) {
		logger.entry();
		setRideRequest(rideRequest);
		int id = rideRequestDAO.create(rideRequestEntity);
		logger.exit();
		return id;
	}

	@Override
	public RideRequest get(int id) {
		rideRequestEntity = rideRequestDAO.get(id);
		if (rideRequestEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRideRequestEntity(rideRequestEntity);
		getRideRequestPoint();
		return rideRequest;
	}

	private void getRideRequestPoint() {
		RideRequestPoint pickupPoint = rideRequestPointDAO.get(rideRequest.getPickupPoint().get_id());
		RideRequestPoint dropPoint = rideRequestPointDAO.get(rideRequest.getDropPoint().get_id());
		rideRequest.setPickupPoint(pickupPoint);
		rideRequest.setDropPoint(dropPoint);;
	}

	@Override
	public RideRequest getChild(int id) {
		get(id);
		fetchChild();
		return rideRequest;
	}

	public int requestRide(RideRequest rideRequest){
		//Setting the travel time and distance, which would be used for searching ride points by date/time as well as sorting the ride request by distance
		RouteDO routeDO = new RouteDO();
		GoogleDistance googleDistance = routeDO.getDistance(rideRequest.getPickupPoint().getPoint(), rideRequest.getDropPoint().getPoint());	
		int travelDistance = googleDistance.getRows().get(0).getElements().get(0).getDistance().getValue();
		int travelTime = googleDistance.getRows().get(0).getElements().get(0).getDuration().getValue();
		rideRequest.setTravelDistance(travelDistance);
		rideRequest.setTravelTime(travelTime);
		
		ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
		rideRequest.setPickupTime(pickupTimeUTC);
		rideRequest.setStatus("unfulfilled");
		int id = create(rideRequest);
		rideRequest.getPickupPoint().setRideRequestId(id);
		rideRequest.getDropPoint().setRideRequestId(id);
		String pickupPointId = rideRequestPointDAO.create(rideRequest.getPickupPoint());
		String dropPointId = rideRequestPointDAO.create(rideRequest.getDropPoint());
		rideRequest.getPickupPoint().set_id(pickupPointId);
		rideRequest.getDropPoint().set_id(dropPointId);
		rideRequest.setId(id);
		update(rideRequest);
		logger.debug("Ride Request has been created with id:" + id);
		return id;
	}
	
	public List<Ride> searchRides(RideRequest rideRequest){		
		
		Point pickupPoint = rideRequest.getPickupPoint().getPoint();
		RidePointDAO ridePointDAO = new RidePointDAO();
		logger.debug("[Pickup Point]:"+pickupPoint.toString());
		//Get all rides around radius of pickup variation from pickup point
		List<RidePoint> pickupRidePoints = ridePointDAO.getAllRidePointNearGivenPoint(pickupPoint, rideRequest.getPickupPointVariation(), 0);
		Point dropPoint = rideRequest.getDropPoint().getPoint();
		logger.debug("[Drop Point]:"+dropPoint.toString());
		List<RidePoint> dropRidePoints = ridePointDAO.getAllRidePointNearGivenPoint(dropPoint, rideRequest.getDropPointVariation(), 0);
		Set<RidePoint> pickupRidePointsSet = new HashSet<>(pickupRidePoints);
		Set<RidePoint> dropRidePointsSet = new HashSet<>(dropRidePoints);
		//All ride pickup points which has matching drop points from the same ride
		pickupRidePointsSet.retainAll(dropRidePointsSet);
		//All ride drop points which has matching pickup points from the same ride
		dropRidePointsSet.retainAll(pickupRidePointsSet);
		Iterator<RidePoint> pickupIterator = pickupRidePointsSet.iterator();
		Iterator<RidePoint> dropIterator = dropRidePointsSet.iterator();
		Map<RidePoint, RidePoint> dropMap = new HashMap<>();
		while (dropIterator.hasNext()){
			RidePoint ridePoint = dropIterator.next();
			dropMap.put(ridePoint, ridePoint);
		}
		
		while(pickupIterator.hasNext()){
			RidePoint pickupRidePoint = pickupIterator.next();
			//This will get the droppoint as "equal" and "hascode" method is based only on ride basic info and not the other details
			RidePoint dropRidePoint = dropMap.get(pickupRidePoint);
			if (pickupRidePoint.getSequence() > dropRidePoint.getSequence() || pickupRidePoint.getSequence() == dropRidePoint.getSequence()){
				//This will remove all ridepoint where drop point comes before pickup point i.e. vehicle going on other direction
				pickupIterator.remove();
				dropMap.remove(dropRidePoint);
			} else {
				logger.debug("Matching Ride:"+pickupRidePoint.getRidesBasicInfo().get(0).getId());
				logger.debug("[Pickup Ride Point]:"+pickupRidePoint.toString());
				logger.debug("[Drop Ride Point]:" + dropRidePoint.toString());
			}
		}

		return null;
	}

}
