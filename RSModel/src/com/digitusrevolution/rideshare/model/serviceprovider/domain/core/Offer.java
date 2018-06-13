package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class Offer implements Comparable<Offer>{
	
	private int id;
	private String name;
	private Photo photo;
	private String description;
	private String termsAndCondition;
	private String redemptionProcess;
	private RedemptionType redemptionType;
	private int ridesRequired;
	private RidesDuration ridesDuration;
	private boolean companyOffer; 
	private Partner partner;
	
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
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTermsAndCondition() {
		return termsAndCondition;
	}
	public void setTermsAndCondition(String termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
	}
	public String getRedemptionProcess() {
		return redemptionProcess;
	}
	public void setRedemptionProcess(String redemptionProcess) {
		this.redemptionProcess = redemptionProcess;
	}
	public RedemptionType getRedemptionType() {
		return redemptionType;
	}
	public void setRedemptionType(RedemptionType redemptionType) {
		this.redemptionType = redemptionType;
	}
	public int getRidesRequired() {
		return ridesRequired;
	}
	public void setRidesRequired(int ridesRequired) {
		this.ridesRequired = ridesRequired;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	public boolean isCompanyOffer() {
		return companyOffer;
	}
	public void setCompanyOffer(boolean companyOffer) {
		this.companyOffer = companyOffer;
	}
	public RidesDuration getRidesDuration() {
		return ridesDuration;
	}
	public void setRidesDuration(RidesDuration ridesDuration) {
		this.ridesDuration = ridesDuration;
	}
	@Override
	public int compareTo(Offer offer) {
		//Negative number is desc order, positive is asc order
		//descending order
		//return Long.compare(offer.id, this.id);

		//ascending order
		return this.id - offer.id;
	}

	
}
