package com.digitusrevolution.rideshare.model.ride.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import com.digitusrevolution.rideshare.model.ride.domain.Geometry;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * This is based on GeoJSON Point Geometry Object specification
 * http://geojson.org/geojson-spec.html
 * 
 * { "type": "Point", "coordinates": [100.0, 0.0] }
 * 
 * The default CRS is a geographic coordinate reference system, using the WGS84 datum, 
 * and with longitude and latitude units of decimal degrees.
 * 
 * The coordinate order is longitude, then latitude as per 
 * 
 * Basic idea is to generate GeoJSON format directly from this POJO
 */

@Embeddable
public class PointEntity implements Geometry{
	
	private final String type = "Point";
	@ElementCollection
	private List<Double> coordinates = new ArrayList<Double>(2);
	
	public PointEntity() {
		//This is just to add two elements in the list, so that while setting lat/lon would not throw index out of bound exception
		coordinates.add(null);
		coordinates.add(null);
	}
	
	public PointEntity(double longitude, double latitude){
		this.coordinates.add(longitude);
		this.coordinates.add(latitude);
	}


	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}

	public String getType() {
		return type;
	}
	
	public double getLongitude(){		
		return getCoordinates().get(0);
	}
	
	public double getLatitude(){		
		return getCoordinates().get(1);
	}
	
	@JsonIgnore
	public void setLongitude(double longitude){		
		getCoordinates().set(0,longitude);
	}
	
	@JsonIgnore
	public void setLatitude(double latitude){		
		getCoordinates().set(1, latitude);
	}
	
	
}
