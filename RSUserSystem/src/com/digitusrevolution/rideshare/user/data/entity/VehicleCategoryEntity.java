package com.digitusrevolution.rideshare.user.data.entity;

import java.util.List;

public class VehicleCategoryEntity {

	private int id;
	private String name;
	private List<VehicleSubCategoryEntity> subCategories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<VehicleSubCategoryEntity> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<VehicleSubCategoryEntity> subCategories) {
		this.subCategories = subCategories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
