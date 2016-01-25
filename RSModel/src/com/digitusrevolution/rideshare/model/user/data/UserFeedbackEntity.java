package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Embeddable
public class UserFeedbackEntity{
	
	@OneToOne
	private UserEntity givenByUser;
	private int rating;
	@OneToOne
	//Each feedback is associated with a ride only
	private RideEntity ride;
	
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
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
		if (!(obj instanceof UserFeedbackEntity)) {
			return false;
		}
		UserFeedbackEntity other = (UserFeedbackEntity) obj;
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
