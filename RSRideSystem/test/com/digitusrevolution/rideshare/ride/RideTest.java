package com.digitusrevolution.rideshare.ride;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.resource.RideDomainResource;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;

public class RideTest {
	
	public static void main(String[] args) {
		
		RideDomainResource rideDomainResource = new RideDomainResource();
		Response response = rideDomainResource.get(1, "true");
		System.out.println(response.toString());
		
	}

}
