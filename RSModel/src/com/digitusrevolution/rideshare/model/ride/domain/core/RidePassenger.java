package com.digitusrevolution.rideshare.model.ride.domain.core;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RidePassenger {
	
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
	
}
