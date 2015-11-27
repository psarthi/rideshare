package com.digitusrevolution.rideshare.user.business.dto;

import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleDTO {
	
	private int userId;
	private Vehicle vehicle;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	

}
