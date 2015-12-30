package com.digitusrevolution.rideshare.poc;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PointTest {
	
 public static void main(String[] args) {
	Point point = new Point();
	point.setLatitude(12.12);
	point.setLongitude(13.12);
	
	Point point1 = new Point(12.45,43.43);
	ObjectMapper mapper = new ObjectMapper();
	String json = null;
	try {
		json = mapper.writeValueAsString(point);
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.out.println(json);
}

}
