package com.digitusrevolution.rideshare.model.ride.domain.core;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RidePassenger {
	
	private User passenger;
	private String status;
	
	public User getPassenger() {
		return passenger;
	}
	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
