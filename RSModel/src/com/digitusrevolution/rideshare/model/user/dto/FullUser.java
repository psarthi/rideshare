package com.digitusrevolution.rideshare.model.user.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.user.domain.FriendRequest;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;

public class FullUser extends BasicUser {
	
	private Collection<BasicGroup> groups = new HashSet<BasicGroup>();
	private Collection<BasicUser> friends = new HashSet<BasicUser>();
	//Don't change this to FullRide or FullRideRequest as it will unnecessarily cause load on the system 
	//which is not required as if we need details Ride Request or Ride, then we should call Ride Server and not User Service
	private Collection<BasicRide> ridesOffered = new HashSet<BasicRide>();
	private Collection<BasicRide> ridesTaken = new HashSet<BasicRide>();
	private Collection<BasicRideRequest> rideRequests = new HashSet<BasicRideRequest>();
	private Collection<Bill> bills = new HashSet<Bill>();
	private Collection<UserFeedback> feedbacks = new LinkedList<UserFeedback>();
	private Collection<FriendRequest> friendRequests = new HashSet<FriendRequest>();	
	private Collection<BasicGroup> groupInvites = new HashSet<BasicGroup>();


	public Collection<BasicGroup> getGroups() {
		return groups;
	}

	public void setGroups(Collection<BasicGroup> groups) {
		this.groups = groups;
	}

	public Collection<BasicGroup> getGroupInvites() {
		return groupInvites;
	}

	public void setGroupInvites(Collection<BasicGroup> groupInvites) {
		this.groupInvites = groupInvites;
	}

	public Collection<BasicUser> getFriends() {
		return friends;
	}

	public void setFriends(Collection<BasicUser> friends) {
		this.friends = friends;
	}

	public Collection<BasicRideRequest> getRideRequests() {
		return rideRequests;
	}

	public void setRideRequests(Collection<BasicRideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}

	public Collection<Bill> getBills() {
		return bills;
	}

	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
	}

	public Collection<BasicRide> getRidesOffered() {
		return ridesOffered;
	}

	public void setRidesOffered(Collection<BasicRide> ridesOffered) {
		this.ridesOffered = ridesOffered;
	}

	public Collection<BasicRide> getRidesTaken() {
		return ridesTaken;
	}

	public void setRidesTaken(Collection<BasicRide> ridesTaken) {
		this.ridesTaken = ridesTaken;
	}

	public Collection<UserFeedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(Collection<UserFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public Collection<FriendRequest> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(Collection<FriendRequest> friendRequests) {
		this.friendRequests = friendRequests;
	}
}





















