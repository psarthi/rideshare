package com.digitusrevolution.rideshare.model.billing.data.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.user.data.core.DriverEntity;
import com.digitusrevolution.rideshare.model.user.data.core.PassengerEntity;

@Entity
@Table(name="bill")
public class BillEntity {
	@Id
	@GeneratedValue
	private int number;
	@ManyToOne
	private PassengerEntity passenger;
	@ManyToOne
	private DriverEntity driver;
	@ManyToOne
	private CompanyEntity company;
	@ManyToOne
	private RideEntity ride;
	private int amount;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public DriverEntity getDriver() {
		return driver;
	}
	public void setDriver(DriverEntity driver) {
		this.driver = driver;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public PassengerEntity getPassenger() {
		return passenger;
	}
	public void setPassenger(PassengerEntity passenger) {
		this.passenger = passenger;
	}
	public CompanyEntity getCompany() {
		return company;
	}
	public void setCompany(CompanyEntity company) {
		this.company = company;
	}
	public RideEntity getRide() {
		return ride;
	}
	public void setRide(RideEntity ride) {
		this.ride = ride;
	}
	

	
}
