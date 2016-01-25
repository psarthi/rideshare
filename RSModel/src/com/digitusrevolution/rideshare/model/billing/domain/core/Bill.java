package com.digitusrevolution.rideshare.model.billing.domain.core;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.core.User;


public class Bill {

	private int number;
	//Reason for storing Passenger, Driver so that we have data handy available with every bill, instead of fetching data from ride/ride request
	//Basic logic of all properties here is to have relationship available at one go instead of doing search again
	private User passenger;
	private User driver;
	private Company company;
	private Ride ride;
	//Reason for rideRequest field, so that we don't have to search ride request number later on if required
	private RideRequest rideRequest;
	private float amount;
	//Reason for storing this value, as if the service charge changes in between, so old bill would not get affected
	private float serviceChargePercentage;
	private BillStatus status;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public User getPassenger() {
		return passenger;
	}
	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}
	public User getDriver() {
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((ride == null) ? 0 : ride.hashCode());
		result = prime * result + ((rideRequest == null) ? 0 : rideRequest.hashCode());
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
		if (!(obj instanceof Bill)) {
			return false;
		}
		Bill other = (Bill) obj;
		if (number != other.number) {
			return false;
		}
		if (ride == null) {
			if (other.ride != null) {
				return false;
			}
		} else if (!ride.equals(other.ride)) {
			return false;
		}
		if (rideRequest == null) {
			if (other.rideRequest != null) {
				return false;
			}
		} else if (!rideRequest.equals(other.rideRequest)) {
			return false;
		}
		return true;
	}
	public float getServiceChargePercentage() {
		return serviceChargePercentage;
	}
	public void setServiceChargePercentage(float serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}
	public RideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(RideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}
	public BillStatus getStatus() {
		return status;
	}
	public void setStatus(BillStatus status) {
		this.status = status;
	}
	
}
