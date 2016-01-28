package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserFeedback{
	
	private User givenByUser;
	private int rating;
	//Each feedback is associated with a ride only
	private Ride ride;


	public User getGivenByUser() {
		return givenByUser;
	}
	public void setGivenByUser(User givenByUser) {
		this.givenByUser = givenByUser;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
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
		result = prime * result + ((givenByUser == null) ? 0 : givenByUser.hashCode());
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
		if (!(obj instanceof UserFeedback)) {
			return false;
		}
		UserFeedback other = (UserFeedback) obj;
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
		return true;
	}
}
