package com.digitusrevolution.rideshare.user.data.entity.core;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity(name="USER")
public class UserEntity {
	
	@Id
	private int id;
	private String firstName;
	private String lastName;
	private Sex sex;
	private String mobileNumber;
	private String email;
	private String password;
	private City city;
	private Photo photo;
	private List<GroupEntity> groups;
	private List<VehicleEntity> vehicles;
	private List<UserEntity> friends;
	private List<Role> roles;
	private List<AccountEntity> accounts;
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
	public List<GroupEntity> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupEntity> groups) {
		this.groups = groups;
	}
	public List<VehicleEntity> getVehicles() {
		return vehicles;
	}
	public void setVehicles(List<VehicleEntity> vehicles) {
		this.vehicles = vehicles;
	}
	public List<UserEntity> getFriends() {
		return friends;
	}
	public void setFriends(List<UserEntity> friends) {
		this.friends = friends;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<AccountEntity> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountEntity> accounts) {
		this.accounts = accounts;
	}
	public int getProfileRating() {
		return profileRating;
	}
	public void setProfileRating(int profileRating) {
		this.profileRating = profileRating;
	}
	
}
