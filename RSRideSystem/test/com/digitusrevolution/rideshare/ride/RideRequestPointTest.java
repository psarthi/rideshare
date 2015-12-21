package com.digitusrevolution.rideshare.ride;


import java.util.List;

import com.digitusrevolution.rideshare.common.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;

public class RideRequestPointTest {
	
	public static void main(String[] args) {
		
		RideRequestPoint rideRequestPoint = new RideRequestPoint();
		rideRequestPoint.getPoint().setLongitude(10.12);
		rideRequestPoint.getPoint().setLatitude(24.23);
		
		RideRequestPointDAO rideRequestPointDAO = new RideRequestPointDAO();
		String _id = rideRequestPointDAO.create(rideRequestPoint);
		
		rideRequestPoint = rideRequestPointDAO.get(_id);
		JSONUtil<RideRequestPoint> jsonUtil = new JSONUtil<>(RideRequestPoint.class);
		System.out.println(jsonUtil.getJson(rideRequestPoint));
		
		rideRequestPoint.getPoint().setLatitude(20.12);
		rideRequestPointDAO.update(rideRequestPoint);
		
		List<RideRequestPoint> rideRequestPoints = rideRequestPointDAO.getAll();
		for (RideRequestPoint rideRequestPoint2 : rideRequestPoints) {
			System.out.println(jsonUtil.getJson(rideRequestPoint2));
			
		}
		
		
		
		
	}

}
