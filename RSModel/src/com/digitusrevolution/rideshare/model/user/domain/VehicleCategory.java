package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

public class VehicleCategory {

	private int id;
	private String name;
	private Collection<VehicleSubCategory> subCategories = new HashSet<VehicleSubCategory>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Collection<VehicleSubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Collection<VehicleSubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
