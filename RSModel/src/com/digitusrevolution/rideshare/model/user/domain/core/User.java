package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.FriendRequest;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.Preference;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class User {
	
	private int id;
	private String firstName;
	private String lastName;
	private Sex sex;
	private String mobileNumber;
	private String email;
	private String password;
	private City city;
	private State state;
	private Country country;
	private Photo photo;
	private Collection<Group> groups = new HashSet<Group>();
	private Collection<Vehicle> vehicles = new HashSet<Vehicle>();
	private Collection<User> friends = new HashSet<User>();
	private Collection<Role> roles = new HashSet<Role>();
	private Collection<Account> accounts = new HashSet<Account>();
	
	private Collection<Ride> ridesOffered = new HashSet<Ride>();
	private Collection<Ride> ridesTaken = new HashSet<Ride>();
	private Collection<RideRequest> rideRequests = new HashSet<RideRequest>();
	private Collection<Bill> bills = new HashSet<Bill>();

	private Preference preference;
	private Collection<UserFeedback> feedbacks = new LinkedList<UserFeedback>();
	private Collection<FriendRequest> friendRequests = new HashSet<FriendRequest>();
	private float profileRating;
	
	private Collection<Group> groupInvites = new HashSet<Group>();
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;		
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public float getProfileRating() {
		return profileRating;
	}

	public void setProfileRating(float profileRating) {
		this.profileRating = profileRating;
	}

	public Collection<Group> getGroups() {
		return groups;
	}

	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}

	public Collection<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Collection<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public Collection<User> getFriends() {
		return friends;
	}

	public void setFriends(Collection<User> friends) {
		this.friends = friends;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Collection<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}

	public Collection<RideRequest> getRideRequests() {
		return rideRequests;
	}

	public void setRideRequests(Collection<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}

	public Collection<Bill> getBills() {
		return bills;
	}

	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
	}

	public Collection<Ride> getRidesOffered() {
		return ridesOffered;
	}

	public void setRidesOffered(Collection<Ride> ridesOffered) {
		this.ridesOffered = ridesOffered;
	}

	public Collection<Ride> getRidesTaken() {
		return ridesTaken;
	}

	public void setRidesTaken(Collection<Ride> ridesTaken) {
		this.ridesTaken = ridesTaken;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	public Account getAccount(AccountType accountType){
		Collection<Account> accounts = getAccounts();
		for (Account account : accounts) {
			if (account.getType().equals(accountType)){
				return account;
			}
		}
		throw new RuntimeException("No account found for the type:"+accountType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + id;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
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
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstName == null) {
			if (other.firstName != null) {
				return false;
			}
		} else if (!firstName.equals(other.firstName)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (lastName == null) {
			if (other.lastName != null) {
				return false;
			}
		} else if (!lastName.equals(other.lastName)) {
			return false;
		}
		if (mobileNumber == null) {
			if (other.mobileNumber != null) {
				return false;
			}
		} else if (!mobileNumber.equals(other.mobileNumber)) {
			return false;
		}
		return true;
	}

	public Collection<UserFeedback> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(Collection<UserFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}

	public Preference getPreference() {
		return preference;
	}

	public void setPreference(Preference preference) {
		this.preference = preference;
	}

	public Collection<FriendRequest> getFriendRequests() {
		return friendRequests;
	}

	public void setFriendRequests(Collection<FriendRequest> friendRequests) {
		this.friendRequests = friendRequests;
	}
	
	public FriendRequest getFriendRequest(int friendUserId){
		Collection<FriendRequest> friendRequests = getFriendRequests();
		for (FriendRequest friendRequest : friendRequests) {
			if (friendRequest.getFriend().getId() == friendUserId){
				return friendRequest;
			}
		}
		//Returning null and not throwing exception, as let this be handled at business logic level
		return null;
	}

	public Collection<Group> getGroupInvites() {
		return groupInvites;
	}

	public void setGroupInvites(Collection<Group> groupInvites) {
		this.groupInvites = groupInvites;
	}

}





















