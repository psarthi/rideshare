package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="user_feedback")
public class UserFeedbackEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private UserEntity forUser;
	@OneToOne
	private UserEntity givenByUser;
	private float rating;
	@OneToOne
	//Each feedback is associated with a ride only
	private RideEntity ride;
	@OneToOne
	private RideRequestEntity rideRequest;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}

	public UserEntity getGivenByUser() {
		return givenByUser;
	}
	public void setGivenByUser(UserEntity givenByUser) {
		this.givenByUser = givenByUser;
	}
	public RideEntity getRide() {
		return ride;
	}
	public void setRide(RideEntity ride) {
		this.ride = ride;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forUser == null) ? 0 : forUser.hashCode());
		result = prime * result + ((givenByUser == null) ? 0 : givenByUser.hashCode());
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
		if (!(obj instanceof UserFeedbackEntity)) {
			return false;
		}
		UserFeedbackEntity other = (UserFeedbackEntity) obj;
		if (forUser == null) {
			if (other.forUser != null) {
				return false;
			}
		} else if (!forUser.equals(other.forUser)) {
			return false;
		}
		if (givenByUser == null) {
			if (other.givenByUser != null) {
				return false;
			}
		} else if (!givenByUser.equals(other.givenByUser)) {
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
	public RideRequestEntity getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(RideRequestEntity rideRequest) {
		this.rideRequest = rideRequest;
	}
	public UserEntity getForUser() {
		return forUser;
	}
	public void setForUser(UserEntity forUser) {
		this.forUser = forUser;
	}
	
}
