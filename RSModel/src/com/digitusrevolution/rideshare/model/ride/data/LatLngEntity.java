package com.digitusrevolution.rideshare.model.ride.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.domain.Geometry;


@Entity
@Table(name="latlng")
public class LatLngEntity implements Geometry{

	@Id
	@GeneratedValue
	private long id;
	private double latitude;
	private double longitude;
	
	//Empty constructor
	public LatLngEntity() {}

	public LatLngEntity(double latitude, double longitude) {
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
