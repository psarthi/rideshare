package com.digitusrevolution.rideshare.model.ride.domain;
import com.digitusrevolution.rideshare.model.ride.domain.Geometry;

public class LatLng implements Geometry{

	private long id;
	private double latitude;
	private double longitude;
	
	//Empty constructor
	public LatLng() {}

	public LatLng(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
