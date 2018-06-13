package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

@Entity
@Table(name="partner")
public class PartnerEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo; 
	private String address;
	private String contactNumber;
	private String email;
	@OneToMany(mappedBy="partner")
	private Collection<OfferEntity> offers = new HashSet<OfferEntity>();
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public PhotoEntity getPhoto() {
		return photo;
	}
	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}
	public Collection<OfferEntity> getOffers() {
		return offers;
	}
	public void setOffers(Collection<OfferEntity> offers) {
		this.offers = offers;
	}

	
}
