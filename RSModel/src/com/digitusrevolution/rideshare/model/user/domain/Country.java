package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.CountryEntity;
import com.digitusrevolution.rideshare.model.user.data.FuelEntity;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;

public class Country implements DomainModel{

	private CountryEntity entity = new CountryEntity();
	private String name;
	private Collection<State> states = new HashSet<State>();
	//Reason for having fuel at country level and not at state level
	//Prices vary on regular basis and number of states are high, so managing around the globe would be difficult
	//Apart from that, variation of fuel prices are not that high, so avg price would do and maintenance would be less
	private Collection<Fuel> fuels = new HashSet<Fuel>();
	private Currency currency = new Currency();

	public String getName() {
		return name; 
	}
	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}
	public Currency getCurrency() {
		currency.setEntity(entity.getCurrency());
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
		entity.setCurrency(currency.getEntity());
	}
	public Collection<State> getStates() {
		if (states.isEmpty()){
			Collection<StateEntity> stateEntities = entity.getStates();
			for (StateEntity stateEntity : stateEntities) {
				State state = new State();
				state.setEntity(stateEntity);
				states.add(state);
			}
		}
		return states;
	}
	public void setStates(Collection<State> states) {
		this.states = states;
		entity.getStates().clear();
		for (State state : states) {
			entity.getStates().add(state.getEntity());
		}
	}

	public void addState(State state){
		states.add(state);
		entity.getStates().add(state.getEntity());
	}

	public Collection<Fuel> getFuels() {
		if (fuels.isEmpty()){
			Collection<FuelEntity> fuelEntities = entity.getFuels();
			for (FuelEntity fuelEntity : fuelEntities) {
				Fuel fuel = new Fuel();
				fuel.setEntity(fuelEntity);
				fuels.add(fuel);
			}
		}
		return fuels;
	}
	public void setFuels(Collection<Fuel> fuels) {
		this.fuels = fuels;
		entity.getFuels().clear();
		for (Fuel fuel : fuels) {
			entity.getFuels().add(fuel.getEntity());
		}
	}
	
	public void addFuel(Fuel fuel){
		fuels.add(fuel);
		entity.getFuels().add(fuel.getEntity());
	}
	
	public CountryEntity getEntity() {
		return entity;
	}
	public void setEntity(CountryEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
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
	@Override
	public void setDomainModelPrimitiveVariable() {
		name = entity.getName();
	}
	@Override
	public void fetchReferenceVariable() {
		getStates();
		getFuels();
		getCurrency();
		
	}
}
