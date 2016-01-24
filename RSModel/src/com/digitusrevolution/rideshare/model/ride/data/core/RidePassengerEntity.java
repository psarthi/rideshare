package com.digitusrevolution.rideshare.model.ride.data.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="ride_passenger")
public class RidePassengerEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private RideEntity ride;
	@ManyToOne
	private UserEntity passenger;
	@Column (name="status")
	@Enumerated(EnumType.STRING)
	private PassengerStatus status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public PassengerStatus getStatus() {
		return status;
	}
	public void setStatus(PassengerStatus status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((passenger == null) ? 0 : passenger.hashCode());
		result = prime * result + ((ride == null) ? 0 : ride.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		if (!(obj instanceof RidePassengerEntity)) {
			return false;
		}
		RidePassengerEntity other = (RidePassengerEntity) obj;
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
		if (status != other.status) {
			return false;
		}
		return true;
	}

	
}
