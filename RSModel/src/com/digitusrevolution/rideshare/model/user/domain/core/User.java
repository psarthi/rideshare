package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.data.FriendRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.data.UserFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.FriendRequest;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.Preference;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class User implements DomainModel{
	
	private UserEntity entity = new UserEntity();
	private int id;
	private String firstName;
	private String lastName;
	private Sex sex;
	private String mobileNumber;
	private String email;
	private String password;
	private City city = new City();
	private State state = new State();
	private Country country = new Country();
	private Photo photo = new Photo();
	private Collection<Group> groups = new HashSet<Group>();
	private Collection<Vehicle> vehicles = new HashSet<Vehicle>();
	private Collection<User> friends = new HashSet<User>();
	private Collection<Role> roles = new HashSet<Role>();
	private Collection<Account> accounts = new HashSet<Account>();
	
	private Collection<Ride> ridesOffered = new HashSet<Ride>();
	private Collection<Ride> ridesTaken = new HashSet<Ride>();
	private Collection<RideRequest> rideRequests = new HashSet<RideRequest>();
	private Collection<Bill> bills = new HashSet<Bill>();

	private Preference preference = new Preference();
	private Collection<UserFeedback> feedbacks = new LinkedList<UserFeedback>();
	private Collection<FriendRequest> friendRequests = new HashSet<FriendRequest>();	
	private float profileRating;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;	
		entity.setId(id);
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		entity.setFirstName(firstName);
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
		entity.setLastName(lastName);
	}
	public Sex getSex() {
		return sex;
	}
	public void setSex(Sex sex) {
		this.sex = sex;
		entity.setSex(sex);
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
		entity.setMobileNumber(mobileNumber);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		entity.setEmail(email);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		entity.setPassword(password);
	}
	public City getCity() {
		city.setEntity(entity.getCity());
		return city;
	}
	public void setCity(City city) {
		this.city = city;
		entity.setCity(city.getEntity());
	}
	public Photo getPhoto() {
		photo.setEntity(entity.getPhoto());
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
		entity.setPhoto(photo.getEntity());
	}

	public float getProfileRating() {
		return profileRating;
	}

	public void setProfileRating(float profileRating) {
		this.profileRating = profileRating;
		entity.setProfileRating(profileRating);
	}

	public Collection<Group> getGroups() {
		Collection<GroupEntity> groupEntities = entity.getGroups();
		for (GroupEntity groupEntity : groupEntities) {
			Group group = new Group();
			group.setEntity(groupEntity);
			groups.add(group);
		}
		return groups;
	}

	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
		for (Group group : groups) {
			entity.getGroups().add(group.getEntity());
		}
	}

	public Collection<Vehicle> getVehicles() {
		Collection<VehicleEntity> vehicleEntities = entity.getVehicles();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			Vehicle vehicle = new Vehicle();
			vehicle.setEntity(vehicleEntity);
			vehicles.add(vehicle);
		}
		return vehicles;
	}

	public void setVehicles(Collection<Vehicle> vehicles) {
		this.vehicles = vehicles;
		for (Vehicle vehicle : vehicles) {
			entity.getVehicles().add(vehicle.getEntity());
		}
	}

	public Collection<User> getFriends() {
		Collection<UserEntity> friendEntities = entity.getFriends();
		for (UserEntity userEntity : friendEntities) {
			User friend = new User();
			friend.setEntity(userEntity);
			friends.add(friend);
		}
		return friends;
	}

	public void setFriends(Collection<User> friends) {
		this.friends = friends;
		for (User user : friends) {
			entity.getFriends().add(user.getEntity());
		}
	}

	public Collection<Role> getRoles() {
		Collection<RoleEntity> roleEntities = entity.getRoles();
		for (RoleEntity roleEntity : roleEntities) {
			Role role = new Role();
			role.setEntity(roleEntity);
			roles.add(role);
		}
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
		for (Role role : roles) {
			entity.getRoles().add(role.getEntity());
		}
	}

	public Collection<Account> getAccounts() {
		Collection<AccountEntity> accountEntities = entity.getAccounts();
		for (AccountEntity accountEntity : accountEntities) {
			Account account = new Account();
			account.setEntity(accountEntity);
			accounts.add(account);
		}
		return accounts;
	}

	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
		for (Account account : accounts) {
			entity.getAccounts().add(account.getEntity());
		}
	}

	public Collection<RideRequest> getRideRequests() {
		Collection<RideRequestEntity> rideRequestEntities = entity.getRideRequests();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			RideRequest rideRequest = new RideRequest();
			rideRequest.setEntity(rideRequestEntity);
			rideRequests.add(rideRequest);
		}
		return rideRequests;
	}

	public void setRideRequests(Collection<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
		for (RideRequest rideRequest : rideRequests) {
			entity.getRideRequests().add(rideRequest.getEntity());
		}
	}

	public Collection<Bill> getBills() {
		Collection<BillEntity> billEntities = entity.getBills();
		for (BillEntity billEntity : billEntities) {
			Bill bill = new Bill();
			bill.setEntity(billEntity);
			bills.add(bill);
		}
		return bills;
	}

	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
		for (Bill bill : bills) {
			entity.getBills().add(bill.getEntity());
		}
	}

	public Collection<Ride> getRidesOffered() {
		Collection<RideEntity> rideEntities = entity.getRidesOffered();
		for (RideEntity rideEntity : rideEntities) {
			Ride ride = new Ride();
			ride.setEntity(rideEntity);
			ridesOffered.add(ride);
		}
		return ridesOffered;
	}

	public void setRidesOffered(Collection<Ride> ridesOffered) {
		this.ridesOffered = ridesOffered;
		for (Ride ride : ridesOffered) {
			entity.getRidesOffered().add(ride.getEntity());
		}
	}

	public Collection<Ride> getRidesTaken() {
		Collection<RidePassengerEntity> ridePassengerEntities = entity.getRidesTaken();
		for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
			Ride ride = new Ride();
			//Note - Our entity and domain model is different for this case, so getting ride from ride passenger
			ride.setEntity(ridePassengerEntity.getRide());
			ridesTaken.add(ride);
		}
		return ridesTaken;
	}

	public void setRidesTaken(Collection<Ride> ridesTaken) {
		this.ridesTaken = ridesTaken;
		//No need to set any ride as our entity and domain model doesn't match here
		//Entity has ride passenger but domain model is storing only ride
	}

	public State getState() {
		state.setEntity(entity.getState());
		return state;
	}

	public void setState(State state) {
		this.state = state;
		entity.setState(state.getEntity());
	}

	public Country getCountry() {
		country.setEntity(entity.getCountry());
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
		entity.setCountry(country.getEntity());
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
		Collection<UserFeedbackEntity> feedbackEntities = entity.getFeedbacks();
		for (UserFeedbackEntity userFeedbackEntity : feedbackEntities) {
			UserFeedback feedback = new UserFeedback();
			feedback.setEntity(userFeedbackEntity);
			feedbacks.add(feedback);
		}
		return feedbacks;
	}

	public void setFeedbacks(Collection<UserFeedback> feedbacks) {
		this.feedbacks = feedbacks;
		for (UserFeedback userFeedback : feedbacks) {
			entity.getFeedbacks().add(userFeedback.getEntity());
		}
	}

	public Preference getPreference() {
		preference.setEntity(entity.getPreference());
		return preference;
	}

	public void setPreference(Preference preference) {
		this.preference = preference;
		entity.setPreference(preference.getEntity());
	}

	public Collection<FriendRequest> getFriendRequests() {
		Collection<FriendRequestEntity> friendRequestEntities = entity.getFriendRequests();
		for (FriendRequestEntity friendRequestEntity : friendRequestEntities) {
			FriendRequest friendRequest = new FriendRequest();
			friendRequest.setEntity(friendRequestEntity);
			friendRequests.add(friendRequest);
		}
		return friendRequests;
	}

	public void setFriendRequests(Collection<FriendRequest> friendRequests) {
		this.friendRequests = friendRequests;
		for (FriendRequest friendRequest : friendRequests) {
			entity.getFriendRequests().add(friendRequest.getEntity());
		}
	}

	public UserEntity getEntity() {
		return entity;
	}

	public void setEntity(UserEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}

	@Override
	public void setDomainModelPrimitiveVariable(){
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		sex = entity.getSex();
		mobileNumber = entity.getMobileNumber();
		email = entity.getEmail();
		password = entity.getPassword();
		profileRating = entity.getProfileRating();
	}
	
}
