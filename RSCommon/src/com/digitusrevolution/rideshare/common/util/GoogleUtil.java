package com.digitusrevolution.rideshare.common.util;

import java.util.List;

import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleGeocode;
import com.digitusrevolution.rideshare.model.ride.dto.google.Result;

public class GoogleUtil {

	public static String getAddress(Double lat, Double lng) {
		
		String response = RESTClientUtil.getReverserGeocode(lat, lng);
		JSONUtil<GoogleGeocode> jsonUtil = new JSONUtil<>(GoogleGeocode.class);
		GoogleGeocode googleGeocodeResult = jsonUtil.getModel(response);		
		String address = null;
		
		if (googleGeocodeResult.getStatus().equals("OK")) {
			List<Result> results = googleGeocodeResult.getResults();
			//1st Priority
			for (Result result : results) {
				for (String type: result.getTypes()) {
					if (type.equals("street_address")) {
						return result.getFormattedAddress();
					}
				}
			}
			//2nd Priority
			for (Result result : results) {
				for (String type: result.getTypes()) {
					if (type.equals("point_of_interest")) {
						return result.getFormattedAddress();
					}
				}
			}
			//3rd Priority
			for (Result result : results) {
				for (String type: result.getTypes()) {
					if (type.equals("route")) {
						return result.getFormattedAddress();
					}
				}
			}
			//4th Priority
			for (Result result : results) {
				for (String type: result.getTypes()) {
					if (type.equals("sublocality")) {
						return result.getFormattedAddress();
					}
				}
			}
		}
		return address;
	}
}
