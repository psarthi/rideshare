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
}
