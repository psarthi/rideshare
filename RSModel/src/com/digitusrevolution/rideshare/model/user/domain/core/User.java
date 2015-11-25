package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

public class User {
	
	private int id;
	private String firstName;
	private String lastName;
	private Sex sex;
	private String mobileNumber;
	private String email;
	private String password;
	private City city;
	private Photo photo;
	private Collection<Group> groups = new ArrayList<Group>();
	private Collection<Vehicle> vehicles = new ArrayList<Vehicle>();
	private Collection<User> friends = new ArrayList<User>();
	private Collection<Role> roles = new ArrayList<Role>();
	private Collection<Account> accounts = new ArrayList<Account>();
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
	public int getProfileRating() {
		return profileRating;
	}
	public void setProfileRating(int profileRating) {
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
	
	
}
