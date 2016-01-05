package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
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
	public void delete(RideRequest rideRequest) {
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

	/*
	 * 
	 * TBD - 
	 * 
	 * 1. What needs to be done when riderequest gets deleted in terms of its riderequest point or its updated
	 */
	public int requestRide(RideRequest rideRequest){
		//Setting the travel time and distance, which would be used for searching ride points by date/time as well as sorting the ride request by distance
		ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
		//Set pickup time in Riderequest pickup point
		RouteDO routeDO = new RouteDO();
		GoogleDistance googleDistance = routeDO.getDistance(rideRequest.getPickupPoint().getPoint(), rideRequest.getDropPoint().getPoint(),pickupTimeUTC);	
		int travelDistance = googleDistance.getRows().get(0).getElements().get(0).getDistance().getValue();
		int travelTime = googleDistance.getRows().get(0).getElements().get(0).getDuration().getValue();

		rideRequest.setTravelDistance(travelDistance);
		rideRequest.setTravelTime(travelTime);
		//Storing dateTime in UTC
		rideRequest.setPickupTime(pickupTimeUTC);
		rideRequest.setStatus("unfulfilled");
		int id = create(rideRequest);

		//No need to get update Ride request as return type as in java its pass by reference, so data would be updated in the original ride request
		setRideRequestPoint(rideRequest, pickupTimeUTC, travelTime, id);

		String pickupPointId = rideRequestPointDAO.create(rideRequest.getPickupPoint());
		String dropPointId = rideRequestPointDAO.create(rideRequest.getDropPoint());
		rideRequest.getPickupPoint().set_id(pickupPointId);
		rideRequest.getDropPoint().set_id(dropPointId);
		rideRequest.setId(id);
		update(rideRequest);
		logger.debug("Ride Request has been created with id:" + id);
		return id;
	}

	private void setRideRequestPoint(RideRequest rideRequest, ZonedDateTime pickupTimeUTC, int travelTime, int id) {
		rideRequest.getPickupPoint().setDateTime(pickupTimeUTC);		
		rideRequest.getDropPoint().setDateTime(pickupTimeUTC.plusSeconds(travelTime));
		rideRequest.getPickupPoint().setRideRequestId(id);
		rideRequest.getDropPoint().setRideRequestId(id);
		rideRequest.getPickupPoint().setDistanceVariation(rideRequest.getPickupPointVariation());
		rideRequest.getDropPoint().setDistanceVariation(rideRequest.getDropPointVariation());
		rideRequest.getPickupPoint().setTimeVariation(rideRequest.getPickupTimeVariation());
		long dropTimeBuffer = Long.parseLong(PropertyReader.getInstance().getProperty("DROP_TIME_BUFFER"));
		rideRequest.getDropPoint().setTimeVariation(rideRequest.getPickupTimeVariation().plusSeconds(dropTimeBuffer));
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

	public FeatureCollection getAllRideRequestPoints(){
		FeatureCollection featureCollection = new FeatureCollection();
		List<RideRequest> rideRequests = getAll();
		for (RideRequest rideRequest : rideRequests) {
			List<RideRequestPoint> requestPoints = rideRequestPointDAO.getRideRequestPointsForRideRequest(rideRequest.getId());
			List<Point> points = new ArrayList<>();
			for (RideRequestPoint rideRequestPoint : requestPoints) {
				points.add(rideRequestPoint.getPoint());
			}
			LineString lineString = GeoJSONUtil.getLineStringFromPoints(points);
			Map<String, Object> properties = new HashMap<>();
			properties.put("Ride Request Id", rideRequest.getId());
			properties.put("Start DateTime in UTC", rideRequest.getPickupTime());
			properties.put("Pickup Variation", rideRequest.getPickupPointVariation());
			properties.put("Drop Variation", rideRequest.getDropPointVariation());
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(lineString, properties);
			featureCollection.add(feature);
		}
		return featureCollection;
	}	

}
