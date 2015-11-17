package com.digitusrevolution.rideshare.user.data.entity;

import java.util.List;

public class CountryEntity {

	private int id;
	private String name;
	private List<StateEntity> states;
	private List<FuelEntity> fuels; 
	private CurrencyEntity currency;
	
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
	public List<StateEntity> getStates() {
		return states;
	}
	public void setStates(List<StateEntity> states) {
		this.states = states;
	}
	public List<FuelEntity> getFuels() {
		return fuels;
	}
	public void setFuels(List<FuelEntity> fuels) {
		this.fuels = fuels;
	}
	public CurrencyEntity getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyEntity currency) {
		this.currency = currency;
	}
	
}
