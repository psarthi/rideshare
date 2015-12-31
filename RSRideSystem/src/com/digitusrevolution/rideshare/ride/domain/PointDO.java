package com.digitusrevolution.rideshare.ride.domain;

import javax.ws.rs.InternalServerErrorException;

import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleGeocode;


public class PointDO{
	
	public Point getCordinates(String address){
		
		String json = RESTClientUtil.getGeocode(address);
		JSONUtil<GoogleGeocode> jsonUtilGoogleGeocode = new JSONUtil<>(GoogleGeocode.class);
		GoogleGeocode googleGeocode = jsonUtilGoogleGeocode.getModel(json);
		if(!googleGeocode.getStatus().equals("OK")){
			throw new InternalServerErrorException("Exception in getting data from Google Geolocation Service:[Status,Error Message]"+googleGeocode.getStatus()+","+googleGeocode.getError_message());
		}
		// This has been added just to get different point object from the same DO,
		// in case you return the DO point then it gets overwritten when you get another points 
		Point point = new Point();
		point.setLatitude(googleGeocode.getResults().get(0).getGeometry().getLocation().getLat());
		point.setLongitude(googleGeocode.getResults().get(0).getGeometry().getLocation().getLng());
		return point;
	}

}
