package com.digitusrevolution.rideshare.ride;

import java.util.Collection;
import java.util.HashMap;

import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.ride.business.RideOfferManagementService;

public class RideSystemTest {
	
	public static void main(String args[]){
		
	
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		Ride ride = new Ride();

		RESTClientUtil restClientUtil = new RESTClientUtil();
		User driver = restClientUtil.getUser(1);
		ride.setDriver(driver);
		
		
		Point point = new Point();
		point.setLattitude("1.12");
		point.setLongitude("2.11");
		
		Point point1 = new Point();
		point1.setLattitude("3.12");
		point1.setLongitude("4.11");

		ride.setStartPoint(point);
		ride.setEndPoint(point1);
		
		TrustCategory trustCategory = new TrustCategory();
		trustCategory.setName("Anonymous");
 
		TrustNetwork trustNetwork = new TrustNetwork();
		trustNetwork.getTrustCategories().add(trustCategory);
		ride.setTrustNetwork(trustNetwork);
		
		Route route = new Route();
		HashMap<Integer, Point> points = new HashMap<>();
		
		Point point2 = new Point();
		point2.setLattitude("5.12");
		point2.setLongitude("6.11");
		
		points.put(1, point);
		points.put(2, point2);
		points.put(3, point1);
		route.setPoints(points);
		
		ride.setRoute(route);
				
		rideOfferManagementService.offerRide(ride);
		
	}

}
