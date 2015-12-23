package com.digitusrevolution.rideshare.ride;


import java.util.List;

import com.digitusrevolution.rideshare.common.db.LocationDAO;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Location;

public class RideRequestPointTest {
	
	public static void main(String[] args) {
		
		Location rideRequestPoint = new Location();
		rideRequestPoint.getPoint().setLongitude(10.12);
		rideRequestPoint.getPoint().setLatitude(24.23);
		
		LocationDAO rideRequestPointDAO = new LocationDAO();
		String _id = rideRequestPointDAO.create(rideRequestPoint);
		
		rideRequestPoint = rideRequestPointDAO.get(_id);
		JSONUtil<Location> jsonUtil = new JSONUtil<>(Location.class);
		System.out.println(jsonUtil.getJson(rideRequestPoint));
		
		rideRequestPoint.getPoint().setLatitude(20.12);
		rideRequestPointDAO.update(rideRequestPoint);
		
		List<Location> rideRequestPoints = rideRequestPointDAO.getAll();
		for (Location rideRequestPoint2 : rideRequestPoints) {
			System.out.println(jsonUtil.getJson(rideRequestPoint2));
			
		}
		
		
		
		
	}

}
