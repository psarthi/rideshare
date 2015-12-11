package com.digitusrevolution.rideshare.ride;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.PolyUtil;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.dto.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.Step;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MathTest {

	public static void main(String[] args) {


		PointDO pointDO = new PointDO();
		Point startPoint = pointDO.getCordinates("Electronic City Bangalore"); 			
		Point endPoint = pointDO.getCordinates("Chennai"); 

		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getDirection(startPoint.getLatitude(),startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude());
		ObjectMapper objectMapper = new ObjectMapper();
		GoogleDirection googleDirection =null;
		try {
			googleDirection = objectMapper.readValue(json, GoogleDirection.class);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to process JSON",e);
		}
		
		List<LatLng> latLngs = PolyUtil.decode(googleDirection.getRoutes().get(0).getOverviewPolyline().getPoints());
		
		LatLng pickupPoint = new LatLng(12.792550, 78.945929); 
		
		boolean status = PolyUtil.isLocationOnPath(pickupPoint, latLngs, true, 40000);
		System.out.println("Location On Edge status: " + status);
		
		LatLng from = new LatLng(12.826930, 77.691434);		
		
		for (LatLng latLng : latLngs) {
			LatLng to = latLng;
//			System.out.println("From: "+from.toUrlValue() + ":To:" + to.toUrlValue() + "Distance: " + SphericalUtil.computeDistanceBetween(from, to));
//			System.out.println(SphericalUtil.computeDistanceBetween(from, to));
			from = to;
		}
		System.out.println("Total Points: "+latLngs.size());
		
		
		List<Step> steps = googleDirection.getRoutes().get(0).getLegs().get(0).getSteps();
		for (Step step : steps) {			
//			System.out.println(step.getStartLocation().getLat() +"," + step.getStartLocation().getLng());
		}


	}
}
