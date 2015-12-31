package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.ZonedDateTime;

public class RideBasicInfo {
	private int id;
	private ZonedDateTime dateTime;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ZonedDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}		
	@Override
	public String toString() {
		return "[id,datetime]:"+id+","+dateTime;
	}
	
	//In case id is changed to long, then use Long.hashcode()
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RideBasicInfo){
			RideBasicInfo rideBasicInfo = (RideBasicInfo) obj;
			return (this.id == rideBasicInfo.id);			
		}
		return false;
	}
}
