package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserFeedback{
	
	private int id;
	private User forUser;
	private User givenByUser;
	private float rating;
	//Each feedback is associated with a ride only
	private Ride ride;
	private RideRequest rideRequest;


	public User getGivenByUser() {
		return givenByUser;
	}
	public void setGivenByUser(User givenByUser) {
		this.givenByUser = givenByUser;
	}
	
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
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
		if (!(obj instanceof UserFeedback)) {
			return false;
		}
		UserFeedback other = (UserFeedback) obj;
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
	public RideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(RideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getForUser() {
		return forUser;
	}
	public void setForUser(User forUser) {
		this.forUser = forUser;
	}
}
