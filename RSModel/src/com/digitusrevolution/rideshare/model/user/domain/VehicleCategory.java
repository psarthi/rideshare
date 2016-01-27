package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;

public class VehicleCategory implements DomainModel{

	private VehicleCategoryEntity entity = new VehicleCategoryEntity();
	private int id;
	private String name;
	private Collection<VehicleSubCategory> subCategories = new HashSet<VehicleSubCategory>();

	public int getId() {
		return id; 
	}

	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}

	public Collection<VehicleSubCategory> getSubCategories() {	
		if (subCategories.isEmpty()){
			Collection<VehicleSubCategoryEntity> subCategoryEntities = entity.getSubCategories();
			for (VehicleSubCategoryEntity vehicleSubCategoryEntity : subCategoryEntities) {
				VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
				vehicleSubCategory.setEntity(vehicleSubCategoryEntity);
				subCategories.add(vehicleSubCategory);
			}
		}
		return subCategories;
	}

	public void setSubCategories(Collection<VehicleSubCategory> subCategories) {
		this.subCategories = subCategories;
		entity.getSubCategories().clear();
		for (VehicleSubCategory vehicleSubCategory : subCategories) {
			entity.getSubCategories().add(vehicleSubCategory.getEntity());
		}
	}
	
	public void addSubCategory(VehicleSubCategory subCategory){
		subCategories.add(subCategory);
		entity.getSubCategories().add(subCategory.getEntity());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}

	public VehicleCategoryEntity getEntity() {
		return entity;
	}

	public void setEntity(VehicleCategoryEntity entity) {
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
		if (!(obj instanceof VehicleCategory)) {
			return false;
		}
		VehicleCategory other = (VehicleCategory) obj;
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

	@Override
	public void fetchReferenceVariable() {
		Collection<VehicleSubCategory> subCategories = getSubCategories();
		for (VehicleSubCategory vehicleSubCategory : subCategories) {
			vehicleSubCategory.fetchReferenceVariable();
		}
	}

}
