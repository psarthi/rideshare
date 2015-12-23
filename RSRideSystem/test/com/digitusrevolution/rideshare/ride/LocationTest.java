package com.digitusrevolution.rideshare.ride;


import java.util.List;

import com.digitusrevolution.rideshare.common.db.LocationDAO;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Location;

public class LocationTest {
	
	public static void main(String[] args) {
		
		Location location = new Location();
		location.getPoint().setLongitude(10.12);
		location.getPoint().setLatitude(24.23);
		
		LocationDAO locationDAO = new LocationDAO();
		String _id = locationDAO.create(location);
		
		location = locationDAO.get(_id);
		JSONUtil<Location> jsonUtil = new JSONUtil<>(Location.class);
		System.out.println(jsonUtil.getJson(location));
		
		location.getPoint().setLatitude(20.12);
		locationDAO.update(location);
		
		List<Location> locations = locationDAO.getAll();
		for (Location location2 : locations) {
			System.out.println(jsonUtil.getJson(location2));
			
		}
		
		
		
		
	}

}
