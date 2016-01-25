package com.digitusrevolution.rideshare.model.ride.domain.core;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

/*
 * Purpose of this class is to break ManyToMany relationship between Ride and User to OneToMany from both sides,
 * so that we can add extra fields of passenger such as Passenger Status
 * 
 */
public class RidePassenger {
	
	private int id;
	private Ride ride;
	private User passenger;
	private PassengerStatus status;
	
	public User getPassenger() {
		return passenger;
	}
	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}
	public PassengerStatus getStatus() {
		return status;
	}
	public void setStatus(PassengerStatus status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((passenger == null) ? 0 : passenger.hashCode());
		result = prime * result + ((ride == null) ? 0 : ride.hashCode());
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
		if (!(obj instanceof RidePassenger)) {
			return false;
		}
		RidePassenger other = (RidePassenger) obj;
		if (id != other.id) {
			return false;
		}
		if (passenger == null) {
			if (other.passenger != null) {
				return false;
			}
		} else if (!passenger.equals(other.passenger)) {
			return false;
		}
		if (ride == null) {
			if (other.ride != null) {
				return false;
			}
		} else if (!ride.equals(other.ride)) {
			return false;
		}
		return true;
	}
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}

}
