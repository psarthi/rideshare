package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

public class Country {

	private String name;
	private Collection<State> states = new HashSet<State>();
	private Collection<Fuel> fuels = new HashSet<Fuel>();
	private Currency currency;
	
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
	
}
