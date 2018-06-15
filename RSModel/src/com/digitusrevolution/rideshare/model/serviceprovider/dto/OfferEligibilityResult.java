package com.digitusrevolution.rideshare.model.serviceprovider.dto;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;

public class OfferEligibilityResult {

	private boolean userEligible;
	private UserRidesStats userRidesStats;
	private Offer offer;
	
	public boolean isUserEligible() {
		return userEligible;
	}
	public void setUserEligible(boolean userEligible) {
		this.userEligible = userEligible;
	}
	public UserRidesStats getUserRidesStats() {
		return userRidesStats;
	}
	public void setUserRidesStats(UserRidesStats userRidesStats) {
		this.userRidesStats = userRidesStats;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	
}
