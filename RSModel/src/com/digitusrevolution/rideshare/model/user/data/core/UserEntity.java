package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity
@Table (name="user_detail")
@Inheritance(strategy=InheritanceType.JOINED)
//@NamedQuery(name="UserEntity.byEmail", query="from UserEntity where email=:email")
public class UserEntity {
	
	@Id
	@GeneratedValue
	private int id;
	private String firstName;
	private String lastName;
	@Column(columnDefinition="varchar(255)")
	private Sex sex;
	private String mobileNumber;
	private String email;
	private String password;
	@ManyToOne
	private CityEntity city;
	@OneToOne
	private PhotoEntity photo;
	@ManyToMany(mappedBy="users")
	private Collection<GroupEntity> groups = new ArrayList<GroupEntity>();
	@OneToMany
	@JoinColumn(name="user_id")
	private Collection<VehicleEntity> vehicles = new ArrayList<VehicleEntity>();
	@ManyToMany
	@JoinTable(name="user_friend",joinColumns=@JoinColumn(name="user_id"))
	private Collection<UserEntity> friends = new ArrayList<UserEntity>();
	@ManyToMany
	@JoinTable(name="user_role",joinColumns=@JoinColumn(name="user_id"))
	private Collection<RoleEntity> roles = new ArrayList<RoleEntity>();
	@OneToMany
	@JoinTable(name="user_account",joinColumns=@JoinColumn(name="user_id"))
	private Collection<AccountEntity> accounts = new ArrayList<AccountEntity>();
	private int profileRating;
	
	
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

	
}
