package com.digitusrevolution.rideshare.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class PlaneGeometry {

	private static final Logger logger = LogManager.getLogger(PlaneGeometry.class.getName());
	
	public double getLineSlope(Point pointA, Point pointB){

		// (Slope) m = (Ya - Yb)/(Xa - Xb) 

		double m = (pointA.getLongitude() - pointB.getLongitude())/(pointA.getLatitude() - pointB.getLatitude());
		return m;
	}

	public double getLineYIntercept(Point point, double slope){

		// y = mx + b, where "m" is slope and "b" is y-intercept 

		double m = slope;
		double b = point.getLongitude() - m * point.getLatitude() ;
		return b;
	}

	public List<Point> getCircleLineIntersectionPoint(Point pointA, Point pointB, Point center, double radius){	

		double m = getLineSlope(pointA, pointB);
		double b = getLineYIntercept(pointA, m);
		double p = center.getLatitude();
		double q = center.getLongitude();
		double r = radius;

		System.out.println("Slope: "+m+"\nY-Intercept:" + b);

		// https://www.sonoma.edu/users/w/wilsonst/papers/Geometry/circles/default.html (Theorem 3.2)
		// If slope is infinite i.e. Vertical line 
		
		if (Double.isInfinite(m)){

			// x1 = x2 as its a vertical line
			double x = pointA.getLatitude();
			double y1 = q + Math.sqrt((Math.pow(r, 2)-Math.pow((pointA.getLatitude()-p),2)));
			double y2 = q - Math.sqrt((Math.pow(r, 2)-Math.pow((pointA.getLatitude()-p),2)));

			System.out.println("Y1,Y2: "+y1+","+y2);

			if (Double.isNaN(y1) && Double.isNaN(y2)){
				System.out.println("No Intersection");
				return Collections.emptyList();
			}

			Point i1 = new Point();
			i1.setLatitude(x);
			i1.setLongitude(y1);
			
			if (y1==y2){
				System.out.println("One Intesection");
				return Arrays.asList(i1);
			}
			
			System.out.println("Two Intesection");
			Point i2 = new Point();
			i2.setLatitude(x);
			i2.setLongitude(y2);
			return Arrays.asList(i1, i2);

		// Reference - http://math.stackexchange.com/questions/228841/how-do-i-calculate-the-intersections-of-a-straight-line-and-a-circle
			
		} else {
			
			double A = Math.pow(m, 2) + 1;
			double B = 2 * (m * b - m * q - p);
			double C = Math.pow(q, 2) - Math.pow(r, 2) + Math.pow(p, 2) - 2 * b * q + Math.pow(b, 2);
			double discriminant = (Math.pow(B, 2) - 4 * A * C);

			if (discriminant < 0){
				System.out.println("No intersection");
				return Collections.emptyList();
			}

			double x1 = (-B + Math.sqrt((discriminant)))/ (2*A);
			double y1 = m * x1 + b ;
			Point i1 = new Point();
			i1.setLatitude(x1);
			i1.setLongitude(y1);

			if (discriminant == 0){
				System.out.println("One Intersection");
				Arrays.asList(i1);
			}

			System.out.println("Two Intersection");			
			double x2 = (-B - Math.sqrt(discriminant))/ (2*A);
			double y2 = m * x2 + b ;
			Point i2 = new Point();
			i2.setLatitude(x2);
			i2.setLongitude(y2);
			return Arrays.asList(i1, i2);
		}

	}
	
	public Point getPerpendicularIntersectionPointOnLineFromAnotherPoint(Point pointA, Point pointB, Point pointC){
		
		double x1 = pointA.getLatitude();
		double y1 = pointA.getLongitude();
		double x2 = pointB.getLatitude();
		double y2 = pointB.getLongitude();
		double x3 = pointC.getLatitude();
		double y3 = pointC.getLongitude();

		/*
		 *  http://stackoverflow.com/questions/1811549/perpendicular-on-a-line-from-a-given-point
		 *  First find equation of line1 from (x1,y1) and (x2,y2) coordinate
		 *  Then find our equation of perpendicular line using reciprocal of slope of line1 and point (x3,y3)
		 *  Once you have both line1 & line2 equation, you can find x,y value which is the co-ordinates of intersection point.
		 *  
		 *  Below formula will arrive once you substitute above points in the equation
		 */
		
		double k = ((y2 - y1) * (x3 - x1) - (x2 - x1) * (y3 - y1))/ (Math.pow((y2-y1), 2) + Math.pow((x2 - x1), 2));
		double x4 = x3 - k * (y2 - y1);
		double y4 = y3 + k * (x2 - x1);
		
		Point perpendicularPoint = new Point(); 
		perpendicularPoint.setLatitude(x4);
		perpendicularPoint.setLongitude(y4);

		return perpendicularPoint;
	}

	public boolean isPointExistBetweenLineSegment(Point startPoint, Point searchPoint, Point endPoint) {
		Point a = startPoint;
		Point b = searchPoint;
		Point c = endPoint;
		double area = (b.getLatitude() - a.getLatitude()) * (c.getLongitude() - a.getLongitude()) 
						- (c.getLatitude() - a.getLatitude()) * (b.getLongitude() - a.getLongitude());
		System.out.println("Area: "+area);
		if (area == 0){
			
			double ac = getDistanceBetweenPoints(a, c); 
			double ab = getDistanceBetweenPoints(a, b);
			double bc = getDistanceBetweenPoints(b, c);
			
			if (ac == (ab + bc)){
				System.out.println("Collinear & Between Line segment");
				return true;
			} else {
				System.out.println("Collinear but not between line segment");
				return false;				
			}
		} else {
			System.out.println("Not Collinear");
			return false;
		}
	}
	
	
	public double getDistanceBetweenPoints(Point a, Point b){
		
		double x = Math.pow((b.getLatitude()-a.getLatitude()),2);
		double y = Math.pow((b.getLongitude()-a.getLongitude()),2);
		double d = Math.sqrt(x-y);
		System.out.println("Distance from "+ a +","+b+":" + d);
		return d;
	}
}