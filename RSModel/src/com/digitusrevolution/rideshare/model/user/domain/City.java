package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.user.data.CityEntity;

public class City{
	
	private CityEntity entity = new CityEntity();
	private int id;
	private String name;
		
	public int getId() {
		id = entity.getId();
		return id; 
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public String getName() {
		name = entity.getName();
		return name;
	}
	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}
	public CityEntity getEntity() {
		return entity;
	}
	public void setEntity(CityEntity entity) {
		this.entity = entity;
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
		if (!(obj instanceof City)) {
			return false;
		}
		City other = (City) obj;
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

}
