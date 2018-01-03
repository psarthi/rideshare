package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RidePoint implements Comparable<RidePoint>{
	
	private String _id;
	//Don't change this to HashMap as serialization / deserialization to JSON would be a problem as 
	//Jackson by default understand only basic datatypes as key and not custom object
	@JsonProperty("rides")
	private List<RidePointProperty> ridePointProperties = new ArrayList<RidePointProperty>();
	private Point point = new Point();
	private int sequence;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}		
	public List<RidePointProperty> getRidePointProperties() {
		return ridePointProperties;
	}
	public void setRidePointProperties(List<RidePointProperty> ridePointProperties) {
		this.ridePointProperties = ridePointProperties;
	}
	@Override
	public String toString(){
		String rides = "";
		for (RidePointProperty ridePointProperty : this.ridePointProperties) {
			rides += ridePointProperty.toString() +":";
		}
		return "[_id:sequence:point:rides]:"+get_id()+":"+getSequence()+":"+getPoint().toString()+":"+rides;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ridePointProperties == null) ? 0 : ridePointProperties.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RidePoint)) {
			return false;
		}
		RidePoint other = (RidePoint) obj;
		if (ridePointProperties == null) {
			if (other.ridePointProperties != null) {
				return false;
			}
		} else if (!ridePointProperties.equals(other.ridePointProperties)) {
			return false;
		}
		return true;
	}
	@Override
	public int compareTo(RidePoint ridePoint) {
		//Negative number is desc order, positive is asc order
		return this.sequence - ridePoint.sequence;
	}
	
	
	
}
