package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.Polygon;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.MathUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
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
	 * This method should be only used from this class
	 * Update functionality is not supported once ride request has been created
	 * 
	 * Note -
	 *
	 * 1. Update of Ride Request Points needs to be well thought - TBD
	 * 
	 */
	@Override
	public void update(RideRequest rideRequest) {
		if (rideRequest.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+rideRequest.getId());
		}
		setRideRequest(rideRequest);
		rideRequestDAO.update(rideRequestEntity);

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

		RideDO rideDO = new RideDO();
		List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(rideId);
		List<Point> centerPoints = new LinkedList<>();
		for (RidePoint ridePoint : ridePoints) {
			centerPoints.add(ridePoint.getPoint());
		}
		List<Point> leftPoints = new LinkedList<>();
		List<Point> rightPoints = new LinkedList<>();
		List<Point> centerReferencePoints = new LinkedList<>();
		LatLng from;
		LatLng to;
		double currentLineHeading;
		double previousLineHeading = 0;
		double distance = Double.parseDouble(PropertyReader.getInstance().getProperty("MAX_DISTANCE_VARIATION_FROM_RIDE_REQUEST_POINT"));
		from = GeoJSONUtil.getLatLng(centerPoints.get(0));

		int i = 0;
		int size = centerPoints.size();
		double SouthHeading = 180;
		double NorthHeading = 0;
		logger.trace("[size]:"+size);


		for (Point point : centerPoints) {
			to = GeoJSONUtil.getLatLng(point);
			logger.trace("[i]:"+i+" :[from,to]:"+"["+from+"],["+to+"]");
			// This will give heading in the range of (-180 to 180) degrees
			// -ve bearing is counterclockwise from North pole and +ve bearing is clocking from north pole 
			currentLineHeading = SphericalUtil.computeHeading(from, to);
			//This will convert -ve heading to +ve when θ = -129.60, then calculation would be (-129.60 + 360) % 360 = 230.39  
			currentLineHeading = MathUtil.convertToCompassBearing(currentLineHeading);
			logger.trace("currentLineHeading, previousLineHeading:"+currentLineHeading+","+previousLineHeading);
			if (i==0){
				//Will not add any point as we have only one point and even though we can add first point, 
				//then the sequence would get effected, we need to first add first point extension and then first point
				i++;
				continue;
			}
			if (i==1){
				addFirstPointAndExtension(leftPoints, rightPoints, centerReferencePoints, from, currentLineHeading,
						distance, NorthHeading);
				from = to;
				previousLineHeading = currentLineHeading;
				i++;				
				continue;
			}

			addPointsAtPerpendicularFromLine(leftPoints, rightPoints, centerReferencePoints, from,
					previousLineHeading, distance);
			
			addPointsAtIntersectionAngle(previousLineHeading, currentLineHeading, from, distance, 
					leftPoints, rightPoints, centerReferencePoints);

			addPointsAtPerpendicularFromLine(leftPoints, rightPoints, centerReferencePoints, from,
					currentLineHeading, distance);

			//This is to get points around last point as well as extension point
			if (i == (size-1)){
				addLastPointAndExtension(leftPoints, rightPoints, centerReferencePoints, to, currentLineHeading,
						distance, SouthHeading);
			}
			previousLineHeading = currentLineHeading;
			from = to;
			i++;
		}

		logger.trace("leftPoint size:"+leftPoints.size());
		createGeoJSONGeometry(leftPoints, rightPoints, centerReferencePoints);

		return null;
	}

	private void addPointsAtIntersectionAngle(double previousLineHeading, double currentLineHeading, LatLng from, double distance, 
			List<Point> leftPoints, List<Point> rightPoints,List<Point> centerReferencePoints){
		//This will get angle between two lines (Current and Previous line)
		double angleBetweenLines = 180 + previousLineHeading - currentLineHeading;
		logger.trace("[angleBetweenLines]:"+angleBetweenLines);
		angleBetweenLines = Math.abs(angleBetweenLines);
		logger.trace("[Math.abs(angleBetweenLines)]:"+angleBetweenLines);
		angleBetweenLines = MathUtil.getMod360(angleBetweenLines);
		logger.trace("[getMod360(angleBetweenLines)]:"+angleBetweenLines);
		double intersectionHeading = currentLineHeading + (angleBetweenLines / 2);
		logger.trace("[angleBetweenLines, intersectionHeading]:"+angleBetweenLines+","+intersectionHeading);
		//Adding 180 so that we get Left point towards North
		intersectionHeading = intersectionHeading + 180;
		logger.trace("[intersectionHeading+180]:"+intersectionHeading);
		intersectionHeading = MathUtil.getMod360(intersectionHeading);
		logger.trace("[Mod360(intersectionHeading)]:"+intersectionHeading);
		logger.trace("[Final - angleBetweenLines, intersectionHeading]:"+angleBetweenLines+","+intersectionHeading);
		logger.trace("Adding Angle Intersection Points");
		//This will add left/right points at intersection angle between lines on the point
		addLeftRightPoints(from, distance, intersectionHeading, leftPoints, rightPoints, centerReferencePoints);

	}

	private void addPointsAtPerpendicularFromLine(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints, LatLng from, double currentLineHeading, double distance) {
		double perpendicularLeftHeading;
		//This will add left/right perpendicular to the line on the point
		//Adding 270 so that we get left point towards North
		perpendicularLeftHeading = currentLineHeading + 270;
		logger.trace("perpendicularLeftHeading = currenHeading + 270: "+perpendicularLeftHeading);
		perpendicularLeftHeading = MathUtil.getMod360(perpendicularLeftHeading);
		logger.trace("Mod360(perpendicularLeftHeading)"+perpendicularLeftHeading);
		logger.trace("Adding Perpendicular Points");
		addLeftRightPoints(from, distance, perpendicularLeftHeading, leftPoints, rightPoints, centerReferencePoints);
	}

	private void addLastPointAndExtension(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints, LatLng to, double currentLineHeading, double distance,
			double NorthHeading) {
		//This will get left/right point from last point
		logger.debug("Adding Last Point");
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(to, distance, NorthHeading, leftPoints, rightPoints, centerReferencePoints);

		logger.debug("Adding Last Point Extension");
		//This will get extension point with the same heading as last point
		LatLng lastExtensionPoint = SphericalUtil.computeOffset(to, distance, currentLineHeading);
		//This will get left/right point from last extension point
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(lastExtensionPoint, distance, NorthHeading, leftPoints, rightPoints,centerReferencePoints);
	}

	private void addFirstPointAndExtension(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints, LatLng from, double currentLineHeading, double distance,
			double NorthHeading) {
		logger.trace("Adding First Point Extension");
		//This is to get first extension point based on first two points
		//Adding 180 to get heading in opposite direction on the same line, so that we can find a point on that line before first point
		double oppsiteDirectionHeading = currentLineHeading + 180;
		oppsiteDirectionHeading = MathUtil.getMod360(oppsiteDirectionHeading);
		logger.trace("oppsiteDirectionHeading:"+oppsiteDirectionHeading);
		//This will get extension point from first point with the heading from second point to first point
		//i.e. on the same line
		LatLng firstExtensionPoint = SphericalUtil.computeOffset(from, distance, oppsiteDirectionHeading);
		//This will get left/right point from first extension point. 
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(firstExtensionPoint, distance, NorthHeading, leftPoints, rightPoints,centerReferencePoints);
		//This is important as from here, all the points require previous heading details 
		//as well as from point needs to be changed to current point
		logger.trace("Adding First Point");
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(from, distance, NorthHeading, leftPoints, rightPoints, centerReferencePoints);
	}

	private void createGeoJSONGeometry(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints) {
		List<Point> reverseRightPoints = new LinkedList<>();
		ListIterator<Point> iterator = rightPoints.listIterator(rightPoints.size());
		while (iterator.hasPrevious()) {
			reverseRightPoints.add(iterator.previous());		
		}

		List<Point> allPoints = new LinkedList<>();
		allPoints.addAll(leftPoints);				
		allPoints.addAll(reverseRightPoints);
		allPoints.add(leftPoints.get(0));

		Polygon polygon = GeoJSONUtil.getPolygonFromPoints(allPoints);
		JSONUtil<Polygon> jsonUtilPolygon = new JSONUtil<>(Polygon.class);
		String polygonGeoJson = jsonUtilPolygon.getJson(polygon);
		logger.trace("polygonGeoJson:"+polygonGeoJson);

		int startIndex = 300;
		int endIndex = 450;

		LineString leftLineString = GeoJSONUtil.getLineStringFromPoints(leftPoints.subList(startIndex, endIndex));
		LineString rightLineString = GeoJSONUtil.getLineStringFromPoints(rightPoints.subList(startIndex, endIndex));
		LineString centerLineString = GeoJSONUtil.getLineStringFromPoints(centerReferencePoints.subList(startIndex, endIndex));
		String geoJsonLeftLineString = GeoJSONUtil.getGeoJsonString(leftLineString);
		String geoJsonRightLineString = GeoJSONUtil.getGeoJsonString(rightLineString);
		String geoJsonCenterLineString = GeoJSONUtil.getGeoJsonString(centerLineString);

		GeometryCollection geometryCollection = new GeometryCollection();
//		geometryCollection.add(leftLineString);
//		geometryCollection.add(rightLineString);
		geometryCollection.add(centerLineString);
		geometryCollection.add(polygon);
		JSONUtil<GeometryCollection> jsonUtil = new JSONUtil<>(GeometryCollection.class);
		logger.trace("Geometry Collection:"+jsonUtil.getJson(geometryCollection));
		logger.trace("Left  :"+geoJsonLeftLineString);
		logger.trace("Right :"+geoJsonRightLineString);
		logger.trace("Center:"+geoJsonCenterLineString);
	}

	private void addLeftRightPoints(LatLng from, double distance, double leftHeading, 
			List<Point> leftPoints, List<Point> rightPoints, List<Point> centerReferencePoints){
		double rightHeading = leftHeading + 180;
		rightHeading = MathUtil.getMod360(rightHeading);
		logger.trace("leftHeading,rightHeading:"+leftHeading+","+rightHeading);
		LatLng left = SphericalUtil.computeOffset(from, distance, leftHeading);
		LatLng right = SphericalUtil.computeOffset(from, distance, rightHeading);
		leftPoints.add(GeoJSONUtil.getPoint(left));
		rightPoints.add(GeoJSONUtil.getPoint(right));
		centerReferencePoints.add(GeoJSONUtil.getPoint(from));
		logger.debug("[Count]:[center,left,right]:"+leftPoints.size()+":["+from+","+left+","+right+"]");
	}
}
