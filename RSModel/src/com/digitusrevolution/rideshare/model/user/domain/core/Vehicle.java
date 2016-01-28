package com.digitusrevolution.rideshare.model.user.domain.core;

import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class Vehicle {

	private int id;
	private VehicleCategory vehicleCategory;
	private VehicleSubCategory vehicleSubCategory;
	private Photo photo;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VehicleSubCategory getVehicleSubCategory() {
		return vehicleSubCategory;
	}

	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((vehicleCategory == null) ? 0 : vehicleCategory.hashCode());
		result = prime * result + ((vehicleSubCategory == null) ? 0 : vehicleSubCategory.hashCode());
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
		if (!(obj instanceof Vehicle)) {
			return false;
		}
		Vehicle other = (Vehicle) obj;
		if (id != other.id) {
			return false;
		}
		if (vehicleCategory == null) {
			if (other.vehicleCategory != null) {
				return false;
			}
		} else if (!vehicleCategory.equals(other.vehicleCategory)) {
			return false;
		}
		if (vehicleSubCategory == null) {
			if (other.vehicleSubCategory != null) {
				return false;
			}
		} else if (!vehicleSubCategory.equals(other.vehicleSubCategory)) {
			return false;
		}
		return true;
	}

}
