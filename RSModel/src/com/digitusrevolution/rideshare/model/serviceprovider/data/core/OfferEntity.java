package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RedemptionType;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RidesDuration;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

@Entity
@Table(name="offer")
public class OfferEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo;
	private String description;
	@Column(columnDefinition="TEXT")
	private String termsAndCondition;
	@Column(columnDefinition="TEXT")
	private String redemptionProcess;
	@Column
	@Enumerated(EnumType.STRING)
	private RedemptionType redemptionType;
	private int ridesRequired;
	@Column
	@Enumerated(EnumType.STRING)
	private RidesDuration ridesDuration;   
	@Type(type="yes_no")
	private boolean companyOffer;
	@OneToOne
	private PartnerEntity partner;
	
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
	public RidesDuration getRidesDuration() {
		return ridesDuration;
	}
	public void setRidesDuration(RidesDuration ridesDuration) {
		this.ridesDuration = ridesDuration;
	}
	public PhotoEntity getPhoto() {
		return photo;
	}
	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}
	public PartnerEntity getPartner() {
		return partner;
	}
	public void setPartner(PartnerEntity partner) {
		this.partner = partner;
	}
	public boolean isCompanyOffer() {
		return companyOffer;
	}
	public void setCompanyOffer(boolean companyOffer) {
		this.companyOffer = companyOffer;
	}

}
