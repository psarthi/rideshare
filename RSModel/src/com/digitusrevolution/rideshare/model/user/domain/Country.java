package com.digitusrevolution.rideshare.model.user.domain;

import java.util.ArrayList;
import java.util.Collection;

public class Country {

	private String name;
	private Collection<State> states = new ArrayList<State>();
	private Collection<Fuel> fuels = new ArrayList<Fuel>();
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
