package com.digitusrevolution.rideshare.model.user.domain.core;

import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class Vehicle {

	private int id;
	private VehicleCategory vehicleCategory;
	private VehicleSubCategory subCategory;
	private Photo photo;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VehicleSubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(VehicleSubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public VehicleCategory getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}	
}
