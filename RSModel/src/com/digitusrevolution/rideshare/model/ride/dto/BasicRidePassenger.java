package com.digitusrevolution.rideshare.model.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Purpose of this class is to break ManyToMany relationship between Ride and User to OneToMany from both sides,
 * so that we can add extra fields of passenger such as Passenger Status
 * 
 */

//Reason behind this jsonignore so that it doesn't throw error while converting from Domain Model to DTO which has less fields
@JsonIgnoreProperties (ignoreUnknown=true)
public class BasicRidePassenger {
	
	private int id;
	private BasicUser passenger;
	private PassengerStatus status;
	
	public BasicUser getPassenger() {
		return passenger;
	}
	public void setPassenger(BasicUser passenger) {
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
}
