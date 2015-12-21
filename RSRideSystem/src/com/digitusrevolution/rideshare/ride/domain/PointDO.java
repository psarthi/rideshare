package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.JSONUtil;
import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleGeocode;


public class PointDO{
	
	public Point getCordinates(String address){
		
		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getGeocode(address);
		JSONUtil<GoogleGeocode> jsonUtil = new JSONUtil<>(GoogleGeocode.class);
		GoogleGeocode googleGeocode = jsonUtil.getModel(json);
		// This has been added just to get different point object from the same DO,
		// in case you return the DO point then it gets overwritten when you get another points 
		Point point = new Point();
		point.setLatitude(googleGeocode.getResults().get(0).getGeometry().getLocation().getLat());
		point.setLongitude(googleGeocode.getResults().get(0).getGeometry().getLocation().getLng());
		return point;
	}

}
