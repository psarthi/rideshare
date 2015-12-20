package com.digitusrevolution.rideshare.ride.domain;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleGeocode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class PointDO{
	
	public Point getCordinates(String address){
		
		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getGeocode(address);
		ObjectMapper objectMapper = new ObjectMapper();
		GoogleGeocode googleGeocode = null;
		try {
			googleGeocode = objectMapper.readValue(json, GoogleGeocode.class);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to process JSON",e);
		}
		// This has been added just to get different point object from the same DO,
		// in case you return the DO point then it gets overwritten when you get another points 
		Point point = new Point();
		point.setLatitude(googleGeocode.getResults().get(0).getGeometry().getLocation().getLat());
		point.setLongitude(googleGeocode.getResults().get(0).getGeometry().getLocation().getLng());
		return point;
	}

}
