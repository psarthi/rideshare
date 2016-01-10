package com.digitusrevolution.rideshare.poc;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.Polygon;

import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.MathUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

public class RideRequestSearchPolyLine {
	
	private static final Logger logger = LogManager.getLogger(RideRequestSearchPolyLine.class.getName());
	
	public void createPolylineAroundRoute(int rideId){

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

		int startIndex = 0;
		int endIndex = leftPoints.size();

		LineString leftLineString = GeoJSONUtil.getLineStringFromPoints(leftPoints.subList(startIndex, endIndex));
		LineString rightLineString = GeoJSONUtil.getLineStringFromPoints(rightPoints.subList(startIndex, endIndex));
		LineString centerLineString = GeoJSONUtil.getLineStringFromPoints(centerReferencePoints.subList(startIndex, endIndex));
		String geoJsonLeftLineString = GeoJSONUtil.getGeoJsonString(leftLineString);
		String geoJsonRightLineString = GeoJSONUtil.getGeoJsonString(rightLineString);
		String geoJsonCenterLineString = GeoJSONUtil.getGeoJsonString(centerLineString);

		GeometryCollection geometryCollection = new GeometryCollection();
		geometryCollection.add(leftLineString);
		geometryCollection.add(rightLineString);
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
