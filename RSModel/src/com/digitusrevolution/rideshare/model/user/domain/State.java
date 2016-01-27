package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;

public class State implements DomainModel{

	private StateEntity entity = new StateEntity();
	private int id;
	private String name;
	private Collection<City> cities = new HashSet<City>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}
	public Collection<City> getCities() {
		if (cities.isEmpty()){
			Collection<CityEntity> cityEntities = entity.getCities();
			for (CityEntity cityEntity : cityEntities) {
				City city = new City();
				city.setEntity(cityEntity);
				cities.add(city);
			}
		}
		return cities;
	}
	public void setCities(Collection<City> cities) {
		this.cities = cities;
		entity.getCities().clear();
		for (City city : cities) {
			entity.getCities().add(city.getEntity());
		}
	}
	
	public void addCity(City city){
		cities.add(city);
		entity.getCities().add(city.getEntity());
	}
	
	public StateEntity getEntity() {
		return entity;
	}
	public void setEntity(StateEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (!(obj instanceof State)) {
			return false;
		}
		State other = (State) obj;
		if (id != other.id) {
			return false;
		}
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
		id = entity.getId();
		name = entity.getName();

	}	
}
