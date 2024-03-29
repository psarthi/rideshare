package com.digitusrevolution.rideshare.poc.core;

import com.digitusrevolution.rideshare.model.ride.domain.Point;

/*
 * Reference - http://www.movable-type.co.uk/scripts/latlong.html
 */
public class SphericalGeometry {

	//Earth Radius in Meters
	private static final int R = 6371000;

	/*
	 * Distance: 
	 * 
	 * This uses the ‘haversine’ formula to calculate the great-circle distance between two points – that is, 
	 * the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’ distance between the points
	 * (ignoring any hills they fly over, of course!).
	 * 
	 * Haversine formula:	
	 * 
	 * a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
	 * c = 2 ⋅ atan2( √a, √(1−a) )
	 * d = R ⋅ c
	 * 
	 * where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
	 * note that angles need to be in radians to pass to trig functions!
	 * 
	 * "c" is called as angular distance which is d / R
	 * 
	 * Returned distance is in meters
	 */

	public double getDistanceByHaversine(Point pointA, Point pointB){

		double φ1 = Math.toRadians(pointA.getLatitude());
		double φ2 = Math.toRadians(pointB.getLatitude());
		double Δφ = Math.toRadians(pointB.getLatitude() - pointA.getLatitude());
		double Δλ = Math.toRadians(pointB.getLongitude() - pointA.getLongitude());

		double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ/2) * Math.sin(Δλ/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R * c;

		return d;		
	}

	/*
	 * Distance:
	 * 
	 * Law of cosines:	d = acos( sin φ1 ⋅ sin φ2 + cos φ1 ⋅ cos φ2 ⋅ cos Δλ ) ⋅ R 
	 *
	 * Returned distance is in meters
	 */

	public double getDistanceByLawOfCosines(Point pointA, Point pointB){

		double φ1 = Math.toRadians(pointA.getLatitude());
		double φ2 = Math.toRadians(pointB.getLatitude());
		double Δλ = Math.toRadians(pointB.getLongitude() - pointA.getLongitude());
		double d = Math.acos(Math.sin(φ1) * Math.sin(φ2) + Math.cos(φ1) * Math.cos(φ2) * Math.cos(Δλ)) * R;

		return d;
	}

	/*
	 * Bearing:
	 * 
	 * This formula is for the initial bearing (sometimes referred to as forward azimuth) 
	 * which if followed in a straight line along a great-circle arc will take you from the start point to the end point
	 * 
	 * Initial bearing is bearing from start point to mid-point
	 * http://www.onlineconversion.com/map_greatcircle_bearings.htm
	 * 
	 * Formula:	θ = atan2( sin Δλ ⋅ cos φ2 , cos φ1 ⋅ sin φ2 − sin φ1 ⋅ cos φ2 ⋅ cos Δλ )
	 *
	 * Since atan2 returns values in the range -π ... +π (that is, -180° ... +180°), to normalize the result to a 
	 * compass bearing (in the range 0° ... 360°, with −ve values transformed into the range 180° ... 360°), 
	 * convert to degrees and then use (θ+360) % 360, where % is (floating point) modulo.
	 * 
	 * for e.g. when θ = -129.60, then calculation would be (-129.60 + 360) % 360 = 230.39
	 * when θ = 168.24, then calculation would be (168.24 + 360) % 360 = 168.24
	 * 
	 * For final bearing, simply take the initial bearing from the end point to the start point and reverse it (using θ = (θ+180) % 360).
	 *
	 */
	public double getInitialBearing(Point pointA, Point pointB){

		double φ1 = Math.toRadians(pointA.getLatitude());
		double φ2 = Math.toRadians(pointB.getLatitude());
		double Δλ = Math.toRadians(pointB.getLongitude() - pointA.getLongitude());
		double y = Math.sin(Δλ) * Math.cos(φ2);
		double x = Math.cos(φ1) * Math.sin(φ2) - Math.sin(φ1) * Math.cos(φ2) * Math.cos(Δλ);
		double θ = Math.atan2(y, x);
//		System.out.println("Original Bearing: "+Math.toDegrees(θ));
		return (Math.toDegrees(θ)+360) % 360;
	}

	/*
	 * Bearing:
	 * 
	 * For final bearing, simply take the initial bearing from the end point to the start point and reverse it (using θ = (θ+180) % 360).
	 * 
	 * Final bearing is actually bearing from mid point to end point 
	 * http://www.onlineconversion.com/map_greatcircle_bearings.htm
	 *
	 */
	public double getFinalBearing(Point pointA, Point pointB){

		double θ = getInitialBearing(pointB, pointA);
		θ = (θ + 180) % 360;
		return θ;
	}


	/*
	 * Midpoint: 
	 * 
	 * This is the half-way point along a great circle path between the two points.
	 * 
	 * Formula:	
	 * 
	 * Bx = cos φ2 ⋅ cos Δλ
	 * By = cos φ2 ⋅ sin Δλ
	 * φm = atan2( sin φ1 + sin φ2, √(cos φ1 + Bx)² + By² )
	 * λm = λ1 + atan2(By, cos(φ1)+Bx)
	 * 
	 * Return Point in degree by converting φm & λm from radians to degree.
	 *
	 */
	public Point getMidpoint (Point pointA, Point pointB){

		double φ1 = Math.toRadians(pointA.getLatitude());
		double φ2 = Math.toRadians(pointB.getLatitude());
		double Δλ = Math.toRadians(pointB.getLongitude() - pointA.getLongitude());
		double λ1 = Math.toRadians(pointA.getLongitude());

		double Bx = Math.cos(φ2) * Math.cos(Δλ);
		double By = Math.cos(φ2) * Math.sin(Δλ);
		double y = Math.sin(φ1) + Math.sin(φ2);
		double x = Math.sqrt((Math.cos(φ1) + Bx) * (Math.cos(φ1) + Bx) + By * By);
		double φm = Math.atan2(y, x);
		double λm = λ1 + Math.atan2(By, Math.cos(φ1) + Bx);
		λm = (λm + 3 * Math.PI) % (2 * Math.PI) - Math.PI;

		Point pointM = new Point(Math.toDegrees(φm),Math.toDegrees(λm));
		return pointM;
	}


	/*
	 * Cross-track distance:
	 * 
	 * Distance of a point from a great-circle path (sometimes called cross track error).
	 * 
	 * Formula:	
	 * dxt = asin( sin(δ13) ⋅ sin(θ13−θ12) ) ⋅ R
	 * 
	 * where, 	
	 * δ13 is (angular) distance from start point to third point
	 * θ13 is (initial) bearing from start point to third point
	 * θ12 is (initial) bearing from start point to end point
	 * R is the earth’s radius
	 * 
	 * Angular distance is = d / R
	 * 
	 * Returns (signed) distance from third point to great circle defined by start-point and end-point.
	 * 
	 * The sign of dxt tells you which side of the path the third point is on, (-ve if to left, +ve if to right of path)
	 * 
	 */

	public double getCrossTrackDistance(Point pointA, Point pointB, Point pointC){

		double δ13 = getDistanceByLawOfCosines(pointA, pointC) / R;
		double θ13 = Math.toRadians(getInitialBearing(pointA, pointC));
		double θ12 = Math.toRadians(getInitialBearing(pointA, pointB));
		double dxt = Math.asin(Math.sin(δ13) * Math.sin(θ13 - θ12)) * R ;
		return dxt;		
	}

	/*
	 * Along-track distance :
	 * 
	 * The along-track distance, from the start point to the closest point on the path to the third point.
	 * 
	 * Formula:	
	 * dat = acos( cos(δ13) / cos(δxt) ) ⋅ R
	 * 
	 * where,
	 * 
	 * δ13 is (angular) distance from start point to third point
	 * δxt is (angular) cross-track distance
	 * R is the earth’s radius
	 * 
	 * Angular distance is = d / R
	 * 
	 */
	
	public double getAlongTrackDistance(Point pointA, Point pointB, Point pointC){
		
		double δ13 = getDistanceByLawOfCosines(pointA, pointC) / R;
		double δxt = getCrossTrackDistance(pointA, pointB, pointC) / R;
		double dat = Math.acos(Math.cos(δ13) / Math.cos(δxt)) * R; 
		return dat;
		
	}
	
	
	/*
	 * Destination point given distance and bearing from start point
	 * 
	 * Given a start point, initial bearing, and distance, 
	 * this will calculate the destination point and final bearing travelling along a (shortest distance) great circle arc.
	 * 
	 * Formula:	
	 * φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
	 * λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 )
	 * where,
	 * φ is latitude, λ is longitude, θ is the bearing (clockwise from north), δ is the angular distance d/R; 
	 * d being the distance travelled, R the earth’s radius
	 * 
	 * 
	 */

	public Point getDestinationPoint(Point point, double bearing, double distance){
		double φ1 = Math.toRadians(point.getLatitude());
		double λ1 = Math.toRadians(point.getLongitude());
		double δ = distance/R;
		double θ = Math.toRadians(bearing);
		
		double φ2 = Math.asin(Math.sin(φ1) * Math.cos(δ) + Math.cos(φ1) * Math.sin(δ) * Math.cos(θ));
		double y = Math.sin(θ) * Math.sin(δ) * Math.cos(φ1);
		double x = Math.cos(δ) - Math.sin(φ1) * Math.sin(φ2);
		double λ2 = λ1 + Math.atan2(y, x);
		Point destinationPoint = new Point(Math.toDegrees(φ2), Math.toDegrees(λ2));
		return destinationPoint;
	}
	
	// Testing (Draft code)
	
	public boolean isOnSegment(Point pointA, Point pointB, Point pointC){

		double φ1 = Math.toRadians(pointA.getLatitude());
		double λ1 = Math.toRadians(pointA.getLongitude());
		double φ2 = Math.toRadians(pointA.getLatitude());
		double λ2 = Math.toRadians(pointA.getLongitude());
		double Δφ12 = Math.toRadians(φ2 - φ1);
		double Δλ12 = Math.toRadians(λ2 - λ1);
		double φ3 = Math.toRadians(pointC.getLatitude());
		double λ3 = Math.toRadians(pointC.getLongitude());
		
		double shortestLength = (Δφ12 * (φ3 - φ1) + Δλ12 * (λ3 - λ1)) / ((Δφ12 * Δφ12) + (Δλ12 * Δλ12));
		double φ4 = φ1 + Δφ12 * shortestLength;
		double λ4 = λ1 + Δλ12 * shortestLength;
		
		if (φ4 < φ2 && φ4 > φ1 && λ4 < λ2 && λ4 > λ1){
			return true;
		} else{
			return false;
		}

	}
	
	
	public static void main(String[] args) {
		SphericalGeometry geometry = new SphericalGeometry();
		Point pointA = new Point(12.839469, 77.676546);
		Point pointB = new Point(12.922471, 77.624018);
		System.out.println("-------------------------------------");
		System.out.println("Details of " + pointA + "," + pointB);
		System.out.println("-------------------------------------");
		double distance = geometry.getDistanceByHaversine(pointA,pointB);
		System.out.println("Haversine - Distance: " + distance);
		System.out.println("Law of Cosine - Distance: " + geometry.getDistanceByLawOfCosines(pointA,pointB));
		double bearning = geometry.getInitialBearing(pointA,pointB);
		System.out.println("Initial Bearing: " + bearning);
		System.out.println("Final Bearing: " + geometry.getFinalBearing(pointA, pointB));
		Point pointC = geometry.getMidpoint(pointA, pointB);
		System.out.println("Middle Point: " + pointC);
		System.out.println("Cross Track Distance from Middle point :" + geometry.getCrossTrackDistance(pointA, pointB, pointC));
		pointC = new Point(12.906074, 77.577326);
		double crossTrackDistance = geometry.getCrossTrackDistance(pointA, pointB, pointC);
		System.out.println("Cross Track Distance from Another point :" + pointC + " : "+ crossTrackDistance);
		double alongTrackDistance = geometry.getAlongTrackDistance(pointA, pointB, pointC);
		System.out.println("Along Track Distance from Another point :" + pointC + " : "+ alongTrackDistance);
		System.out.println("Destination Point: " + geometry.getDestinationPoint(pointA,bearning, distance));
		pointC = geometry.getDestinationPoint(pointA,bearning, alongTrackDistance);
		System.out.println("Destination Point: " + pointC);
		System.out.println("Distance: "+geometry.getDistanceByHaversine(pointA, pointC));
		

		pointA = new Point(13.032170, 77.640785);
		pointB = new Point(29.426006, 77.201332);
		System.out.println("-------------------------------------");
		System.out.println("Details of " + pointA + "," + pointB);
		System.out.println("-------------------------------------");
		distance = geometry.getDistanceByHaversine(pointA,pointB);
		System.out.println("Haversine - Distance: " + distance);
		System.out.println("Law of Cosine - Distance: " + geometry.getDistanceByLawOfCosines(pointA,pointB));
		bearning = geometry.getInitialBearing(pointA,pointB);
		System.out.println("Initial Bearing: " + bearning);
		System.out.println("Final Bearing: " + geometry.getFinalBearing(pointA, pointB));
		pointC = geometry.getMidpoint(pointA, pointB);
		System.out.println("Middle Point: " + pointC);		
		System.out.println("Cross Track Distance from Middle point :" + geometry.getCrossTrackDistance(pointA, pointB, pointC));
		pointC = new Point(29.811668, 76.839084);
		crossTrackDistance = geometry.getCrossTrackDistance(pointA, pointB, pointC);
		System.out.println("Cross Track Distance from Another point :" + pointC + " : "+ crossTrackDistance);
		alongTrackDistance = geometry.getAlongTrackDistance(pointA, pointB, pointC);
		System.out.println("Along Track Distance from Another point :" + pointC + " : "+ alongTrackDistance);
	}

}
