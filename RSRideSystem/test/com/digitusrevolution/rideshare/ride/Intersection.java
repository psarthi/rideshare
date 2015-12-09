package com.digitusrevolution.rideshare.ride;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Intersection {

	public static double getLineSlope(Point pointA, Point pointB){

		// (Slope) m = (Ya - Yb)/(Xa - Xb) 

		double m = (pointA.y - pointB.y)/(pointA.x - pointB.x);
		return m;

	}

	public static double getLineYIntercept(Point point, double m){

		// y = mx + b, where "m" is slope and "b" is y-intercept 

		double b = point.y - m * point.x ;
		return b;
	}

	public static List<Point> getCircleLineIntersectionPoint(Point pointA, Point pointB, Point center, double radius){	

		double m = getLineSlope(pointA, pointB);
		double b = getLineYIntercept(pointA, m);
		double p = center.x;
		double q = center.y;
		double r = radius;
		double A = Math.pow(m, 2) + 1;
		double B = 2 * (m * b - m * q - p);
		double C = Math.pow(q, 2) - Math.pow(r, 2) + Math.pow(p, 2) - 2 * b * q + Math.pow(b, 2);
		double discriminant = (Math.pow(B, 2) - 4 * A * C);

		System.out.println("Slope: "+m+"\nY-Intercept:" + b);

		// https://www.sonoma.edu/users/w/wilsonst/papers/Geometry/circles/default.html (Theorem 3.2)
		// If slope is infinite i.e. Vertical line 
		if (Double.isInfinite(m)){

			double y1 = q + Math.sqrt((Math.pow(r, 2)-Math.pow((pointA.x-p),2)));
			double y2 = q - Math.sqrt((Math.pow(r, 2)-Math.pow((pointA.x-p),2)));
			System.out.println("Y1,Y2: "+y1+","+y2);

			if (Double.isNaN(y1) && Double.isNaN(y2)){
				System.out.println("No Intersection");
				return Collections.emptyList();
			}

			Point i1 = new Point(pointA.x, y1);			
			if (y1==y2){
				System.out.println("One Intesection");
				return Arrays.asList(i1);
			}
			System.out.println("Two Intesection");
			Point i2 = new Point(pointA.x, y2);
			Point perpendicularPoint = getPerpendicularIntersectionPointFromAnotherPoint(i1, i2, center);
			return Arrays.asList(i1, i2, perpendicularPoint);

		// Reference - http://math.stackexchange.com/questions/228841/how-do-i-calculate-the-intersections-of-a-straight-line-and-a-circle
		} else {

			if (discriminant < 0){
				System.out.println("No intersection");
				return Collections.emptyList();
			}

			double x1 = (-B + Math.sqrt((discriminant)))/ (2*A);
			double y1 = m * x1 + b ;
			Point i1 = new Point(x1, y1);

			if (discriminant == 0){
				System.out.println("One Intersection");
				Arrays.asList(i1);
			}

			double x2 = (-B - Math.sqrt(discriminant))/ (2*A);
			double y2 = m * x2 + b ;
			Point i2 = new Point(x2, y2);

			System.out.println("Two Intersection");
			
			Point perpendicularPoint = getPerpendicularIntersectionPointFromAnotherPoint(i1, i2, center);			
			return Arrays.asList(i1, i2, perpendicularPoint);
		}

	}
	
	public static Point getPerpendicularIntersectionPointFromAnotherPoint(Point pointA, Point pointB, Point pointC){
		
		double x1 = pointA.x;
		double y1 = pointA.y;
		double x2 = pointB.x;
		double y2 = pointB.y;
		double x3 = pointC.x;
		double y3 = pointC.y;

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
		
		Point perpendicularPoint = new Point(x4, y4);

		return perpendicularPoint;
	}


	static class Point{
		double x, y;

		public Point(double x, double y) { this.x = x; this.y = y; }

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}
	}

	public static void main(String[] args) {

		System.out.println(getCircleLineIntersectionPoint(new Point(-3, -3),
				new Point(-3, 3), new Point(0, 0), 4));
		System.out.println(getCircleLineIntersectionPoint(new Point(0, -2),
				new Point(1, -2), new Point(1, 1), 5));
		System.out.println(getCircleLineIntersectionPoint(new Point(1, -1),
				new Point(-1, 0), new Point(-1, 1), 5));
		System.out.println(getCircleLineIntersectionPoint(new Point(-3, -3),
				new Point(-2, -2), new Point(0, 0), Math.sqrt(2)));
		
		System.out.println(getPerpendicularIntersectionPointFromAnotherPoint(new Point(9, 5), new Point(49, 5), new Point(3, 11)));
		
	}
}