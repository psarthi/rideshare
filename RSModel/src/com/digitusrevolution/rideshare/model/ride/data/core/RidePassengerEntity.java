package com.digitusrevolution.rideshare.model.ride.data.core;

import java.beans.Transient;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="ride_passenger")
@AssociationOverrides({
    @AssociationOverride(name = "primaryKey.passenger",
        joinColumns = @JoinColumn(name = "passenger_id")),
    @AssociationOverride(name = "primaryKey.ride",
        joinColumns = @JoinColumn(name = "ride_id")) })
public class RidePassengerEntity {
	
	//composite-id key
	@EmbeddedId
    private RidePassengerId primaryKey = new RidePassengerId();
	@Column (name="status")
	@Enumerated(EnumType.STRING)
	private PassengerStatus status;

	public RidePassengerId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(RidePassengerId primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Transient
	public RideEntity getRide() {
		return getPrimaryKey().getRide();
	}
	public void setRide(RideEntity ride) {
		getPrimaryKey().setRide(ride);
	}
	
	@Transient
	public UserEntity getPassenger() {
		return getPrimaryKey().getPassenger();
	}
	public void setPassenger(UserEntity passenger) {
		getPrimaryKey().setPassenger(passenger);
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
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
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
		if (primaryKey == null) {
			if (other.primaryKey != null) {
				return false;
			}
		} else if (!primaryKey.equals(other.primaryKey)) {
			return false;
		}
		return true;
	}
	

	
}
