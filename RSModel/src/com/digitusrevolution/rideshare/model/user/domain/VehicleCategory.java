package com.digitusrevolution.rideshare.model.user.domain;

import java.util.List;

public class VehicleCategory {

	private int id;
	private String name;
	private List<VehicleSubCategory> subCategories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<VehicleSubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<VehicleSubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
