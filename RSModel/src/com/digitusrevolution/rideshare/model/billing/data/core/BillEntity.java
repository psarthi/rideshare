package com.digitusrevolution.rideshare.model.billing.data.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="bill")
public class BillEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int number;
	@ManyToOne
	private UserEntity passenger;
	@ManyToOne
	private UserEntity driver;
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
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
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
	public UserEntity getPassenger() {
		return passenger;
	}
	public void setPassenger(UserEntity passenger) {
		this.passenger = passenger;
	}
	public UserEntity getDriver() {
		return driver;
	}
	public void setDriver(UserEntity driver) {
		this.driver = driver;
	}
	

	
}
