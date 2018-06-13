package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class Partner {

	private int id;
	private String name;
	private Photo photo; 
	private String address;
	private String contactNumber;
	private String email;
	private Collection<Offer> offers = new HashSet<Offer>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public Collection<Offer> getOffers() {
		return offers;
	}
	public void setOffers(Collection<Offer> offers) {
		this.offers = offers;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
