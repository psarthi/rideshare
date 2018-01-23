package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;

public class Country {

	private String name;
	private Collection<State> states = new HashSet<State>();
	//Reason for having fuel at country level and not at state level
	//Prices vary on regular basis and number of states are high, so managing around the globe would be difficult
	//Apart from that, variation of fuel prices are not that high, so avg price would do and maintenance would be less
	private Collection<Fuel> fuels = new HashSet<Fuel>();
	private Currency currency;
	private String code;
	private RideMode rideMode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public Collection<State> getStates() {
		return states;
	}
	public void setStates(Collection<State> states) {
		this.states = states;
	}
	public Collection<Fuel> getFuels() {
		return fuels;
	}
	public void setFuels(Collection<Fuel> fuels) {
		this.fuels = fuels;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof Country)) {
			return false;
		}
		Country other = (Country) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public RideMode getRideMode() {
		return rideMode;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
	}

}
