package com.digitusrevolution.rideshare.ride;

import java.util.List;

import com.digitusrevolution.rideshare.common.MathUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class MathUtilTest {
	

	public static void main(String[] args) {

		MathUtil mathUtil = new MathUtil();
		Point pointA = new Point();
		pointA.setLatitude(-3);
		pointA.setLongitude(-3);
		
		Point pointB = new Point();
		pointB.setLatitude(-3);
		pointB.setLongitude(3);
		
		Point center = new Point();
		center.setLatitude(0);
		center.setLongitude(0);
		
		List<Point> points = mathUtil.getCircleLineIntersectionPoint(pointA,pointB,center,3);
		System.out.println("Intersection Points: "+ points);

		if (points.size()==2){
			Point point = mathUtil.getPerpendicularIntersectionPointOnLineFromAnotherPoint(pointA,pointB,center);
			System.out.println("Perpendicular Point from Center: " + point);
		}

		pointA.setLatitude(0);
		pointA.setLongitude(-2);
		
		pointB.setLatitude(1);
		pointB.setLongitude(-2);
		
		center.setLatitude(1);
		center.setLongitude(1);

		
		points = mathUtil.getCircleLineIntersectionPoint(pointA,pointB,center, 5);
		System.out.println("Intersection Points: "+ points);
		
		if (points.size()==2){
			Point point = mathUtil.getPerpendicularIntersectionPointOnLineFromAnotherPoint(pointA,pointB,center);
			System.out.println("Perpendicular Point from Center: " + point);
		}
		
		pointA.setLatitude(9);
		pointA.setLongitude(5);
		
		pointB.setLatitude(49);
		pointB.setLongitude(5);
		
		center.setLatitude(3);
		center.setLongitude(11);

		System.out.println(mathUtil.getPerpendicularIntersectionPointOnLineFromAnotherPoint(pointA,pointB,center));
		
		pointA.setLatitude(1);
		pointA.setLongitude(0);
		
		pointB.setLatitude(2);
		pointB.setLongitude(0);
		
		Point pointC = new Point();
		pointC.setLatitude(3);
		pointC.setLongitude(0);
		
		System.out.println(mathUtil.isPointExistBetweenLineSegment(pointA,pointB,pointC));
		
		
	}

}
