package com.digitusrevolution.rideshare.model.user.data;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
	@JoinTable(name="state_city",joinColumns=@JoinColumn(name="state_id"))
	private Collection<CityEntity> cities = new ArrayList<CityEntity>();
	
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
	public Collection<CityEntity> getCities() {
		return cities;
	}
	public void setCities(Collection<CityEntity> cities) {
		this.cities = cities;
	}
	
}
