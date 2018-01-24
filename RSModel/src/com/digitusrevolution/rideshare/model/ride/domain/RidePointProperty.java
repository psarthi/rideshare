package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.ZonedDateTime;

public class RidePointProperty {
	private long id;
	private ZonedDateTime dateTime;

	public long getId() {
		return id;
	}
	public void setId(long id) {
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
		return "[rideId,datetime]:"+id+","+dateTime;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof RidePointProperty)) {
			return false;
		}
		RidePointProperty other = (RidePointProperty) obj;
		if (dateTime == null) {
			if (other.dateTime != null) {
				return false;
			}
		} else if (!dateTime.equals(other.dateTime)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		return true;
	}	
	
	
}
