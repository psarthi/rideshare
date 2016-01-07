package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RideRequestDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;


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

	/*
	 * This method is not supported i.e. riderequest once created can't be updated due to complexity involved
	 * 
	 * Note -
	 *
	 * 1. Update of Ride Request Points needs to be well thought - TBD
	 * 
	 */
	@Override
	public void update(RideRequest rideRequest) {
		throw new NotAuthorizedException("Ride Request update is not supported");
		
		//Below code needs to be uncommented and modified as per business rules required for update
		/*		if (rideRequest.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+rideRequest.getId());
		}
		setRideRequest(rideRequest);
		rideRequestDAO.update(rideRequestEntity);
		 */	
	}

	@Override
	public void delete(int id) {
		rideRequest = get(id);
		setRideRequest(rideRequest);
		rideRequestDAO.delete(rideRequestEntity);
		rideRequestPointDAO.deletePointsOfRideRequest(id);
	}
	
	/*
	 * This method should not be used from external classes and instead use requestRide method
	 * This method is only used internally from requestRide
	 * 
	 * Issue - 
	 * 
	 * 1. How to make this method private
	 * 
	 */
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

	/*
	 * This method doesn't return but set ride request points in the ride request object itself 
	 */
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
	 * Purpose - Ride Request needs to be created with corresponding ride request points
	 * 
	 * High Level Logic -
	 * 
	 * - Convert pickup time to UTC
	 * - Set the status
	 * - Create Ride Request in DB
	 * - Set Ride Request point properties and then create them in NoSQL DB 
	 * 
	 */
	public int requestRide(RideRequest rideRequest){
		ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
		//Storing dateTime in UTC
		rideRequest.setPickupTime(pickupTimeUTC);
		rideRequest.setStatus("unfulfilled");
		int id = create(rideRequest);
		rideRequest.setId(id);

		//No need to get update Ride request as return type as in java its pass by reference, so data would be updated in the original ride request
		setRideRequestPoint(rideRequest);
		
		String pickupPointId = rideRequestPointDAO.create(rideRequest.getPickupPoint());
		String dropPointId = rideRequestPointDAO.create(rideRequest.getDropPoint());
		rideRequest.getPickupPoint().set_id(pickupPointId);
		rideRequest.getDropPoint().set_id(dropPointId);
		rideRequest.setId(id);
		update(rideRequest);
		logger.debug("Ride Request has been created with id:" + id);
		return id;
	}

	private void setRideRequestPoint(RideRequest rideRequest) {
		rideRequest.getPickupPoint().setDateTime(rideRequest.getPickupTime());		
		rideRequest.getDropPoint().setDateTime(rideRequest.getPickupTime().plusSeconds(rideRequest.getTravelTime()));
		rideRequest.getPickupPoint().setRideRequestId(rideRequest.getId());
		rideRequest.getDropPoint().setRideRequestId(rideRequest.getId());
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
	public List<RideRequest> searchRideRequests(int rideId){

		return null;
	}

	public FeatureCollection getAllRideRequestPoints(){
		FeatureCollection featureCollection = new FeatureCollection();

		List<RideRequest> rideRequests = getAll();
		for (RideRequest rideRequest : rideRequests) {
			List<RideRequestPoint> requestPoints = getPointsOfRideRequest(rideRequest.getId());			
			for (RideRequestPoint rideRequestPoint : requestPoints) {
				if (rideRequestPoint.get_id().equals(rideRequest.getPickupPoint().get_id())){
					Point pickupPoint = rideRequestPoint.getPoint();
					Map<String, Object> properties = getRideRequestPointProperties(rideRequestPoint, "pickuppoint");
					org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(pickupPoint);
					Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, properties);
					featureCollection.add(feature);
				} else {					
					Point dropPoint = rideRequestPoint.getPoint();
					Map<String, Object> properties = getRideRequestPointProperties(rideRequestPoint, "droppoint");
					org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(dropPoint);
					Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, properties);
					featureCollection.add(feature);
				}
			}
		}	

		return featureCollection;
	}

	private Map<String, Object> getRideRequestPointProperties(RideRequestPoint rideRequestPoint, String pointType) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("type", pointType);
		properties.put("RideRequestId", rideRequestPoint.getRideRequestId());
		properties.put("DateTimeUTC", rideRequestPoint.getDateTime());
		properties.put("DistanceVariation", rideRequestPoint.getDistanceVariation());
		return properties;
	}

	public List<RideRequestPoint> getPointsOfRideRequest(int rideRequestId) {
		return rideRequestPointDAO.getPointsOfRideRequest(rideRequestId);
	}

}
