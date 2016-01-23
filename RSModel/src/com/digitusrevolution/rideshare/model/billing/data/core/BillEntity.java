package com.digitusrevolution.rideshare.model.billing.data.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
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
	@OneToOne
	private RideRequestEntity rideRequest;
	private float amount;
	private float serviceChargePercentage;
	@Column (name="status")
	@Enumerated(EnumType.STRING)
	private BillStatus billStatus;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
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
		if (!(obj instanceof BillEntity)) {
			return false;
		}
		BillEntity other = (BillEntity) obj;
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
		return true;
	}
	public float getServiceChargePercentage() {
		return serviceChargePercentage;
	}
	public void setServiceChargePercentage(float serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}
	public RideRequestEntity getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(RideRequestEntity rideRequest) {
		this.rideRequest = rideRequest;
	}
	public BillStatus getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(BillStatus billStatus) {
		this.billStatus = billStatus;
	}
	

	
}
