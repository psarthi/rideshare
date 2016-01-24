package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.CountryEntity;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity
@Table (name="user_detail")
//@NamedQuery(name="UserEntity.byEmail", query="from UserEntity where email=:email")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String firstName;
	private String lastName;
	@Column
	@Enumerated(EnumType.STRING)
	private Sex sex;
	private String mobileNumber;
	private String email;
	private String password;
	@ManyToOne
	private CityEntity city;
	@ManyToOne
	private StateEntity state;
	@ManyToOne
	private CountryEntity country;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo;
	@ManyToMany(mappedBy="users")
	private Collection<GroupEntity> groups = new HashSet<GroupEntity>();
	@OneToMany(cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private Collection<VehicleEntity> vehicles = new HashSet<VehicleEntity>();
	@ManyToMany
	@JoinTable(name="user_friend",joinColumns=@JoinColumn(name="user_id"))
	private Collection<UserEntity> friends = new HashSet<UserEntity>();
	@ManyToMany
	@JoinTable(name="user_role",joinColumns=@JoinColumn(name="user_id"))
	private Collection<RoleEntity> roles = new HashSet<RoleEntity>();
	@OneToMany
	@JoinTable(name="user_account",joinColumns=@JoinColumn(name="user_id"))
	private Collection<AccountEntity> accounts = new HashSet<AccountEntity>();
	private int profileRating;
	
	@OneToMany(mappedBy="driver")
	private Collection<RideEntity> ridesOffered = new HashSet<RideEntity>();
	
	//Reason for Many to Many relationship, as one user can take many rides and 
	//one ride can have many users as passenger
	@OneToMany(mappedBy="passenger")
	private Collection<RidePassengerEntity> ridesTaken = new HashSet<RidePassengerEntity>();
	//Reason for One to Many relationship, as one user can raise many ride request
	//but one ride request can be raised by only one user acting as passenger
	@OneToMany(mappedBy="passenger")
	private Collection<RideRequestEntity> rideRequests = new HashSet<RideRequestEntity>();
	//Reason for One to Many relationship, One user can have many bills
	@OneToMany(mappedBy="passenger")
	private Collection<BillEntity> bills = new HashSet<BillEntity>();

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

	public int getProfileRating() {
		return profileRating;
	}
	public void setProfileRating(int profileRating) {
		this.profileRating = profileRating;
	}

	public CityEntity getCity() {
		return city;
	}

	public void setCity(CityEntity city) {
		this.city = city;
	}

	public PhotoEntity getPhoto() {
		return photo;
	}

	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}

	public Collection<GroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(Collection<GroupEntity> groups) {
		this.groups = groups;
	}

	public Collection<VehicleEntity> getVehicles() {
		return vehicles;
	}

	public void setVehicles(Collection<VehicleEntity> vehicles) {
		this.vehicles = vehicles;
	}

	public Collection<UserEntity> getFriends() {
		return friends;
	}

	public void setFriends(Collection<UserEntity> friends) {
		this.friends = friends;
	}

	public Collection<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleEntity> roles) {
		this.roles = roles;
	}

	public Collection<AccountEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(Collection<AccountEntity> accounts) {
		this.accounts = accounts;
	}

	public Collection<RideRequestEntity> getRideRequests() {
		return rideRequests;
	}

	public void setRideRequests(Collection<RideRequestEntity> rideRequests) {
		this.rideRequests = rideRequests;
	}

	public Collection<BillEntity> getBills() {
		return bills;
	}

	public void setBills(Collection<BillEntity> bills) {
		this.bills = bills;
	}

	public Collection<RideEntity> getRidesOffered() {
		return ridesOffered;
	}

	public void setRidesOffered(Collection<RideEntity> ridesOffered) {
		this.ridesOffered = ridesOffered;
	}

	public Collection<RidePassengerEntity> getRidesTaken() {
		return ridesTaken;
	}

	public void setRidesTaken(Collection<RidePassengerEntity> ridesTaken) {
		this.ridesTaken = ridesTaken;
	}

	public StateEntity getState() {
		return state;
	}

	public void setState(StateEntity state) {
		this.state = state;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
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
		if (!(obj instanceof UserEntity)) {
			return false;
		}
		UserEntity other = (UserEntity) obj;
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

}
