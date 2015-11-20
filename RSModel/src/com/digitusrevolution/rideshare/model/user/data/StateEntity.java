package com.digitusrevolution.rideshare.model.user.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="state")
public class StateEntity {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany
	private List<CityEntity> cities;
	
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
	public List<CityEntity> getCities() {
		return cities;
	}
	public void setCities(List<CityEntity> cities) {
		this.cities = cities;
	}
	
}
