package com.digitusrevolution.rideshare.model.serviceprovider.dto;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;

public class UserOffer extends Offer {
	
	private boolean userEligible;
	private int balanceRideRequirement;

	public boolean isUserEligible() {
		return userEligible;
	}

	public void setUserEligible(boolean userEligible) {
		this.userEligible = userEligible;
	}

	public int getBalanceRideRequirement() {
		return balanceRideRequirement;
	}

	public void setBalanceRideRequirement(int balanceRideRequirement) {
		this.balanceRideRequirement = balanceRideRequirement;
	}

}
