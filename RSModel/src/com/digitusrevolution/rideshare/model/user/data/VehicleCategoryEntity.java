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
@Table(name="vehicle_category")
public class VehicleCategoryEntity {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany
	@JoinTable(name="vehicle_category_subCategory",joinColumns=@JoinColumn(name="vehicle_category_id"))
	private Collection<VehicleSubCategoryEntity> subCategories = new ArrayList<VehicleSubCategoryEntity>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Collection<VehicleSubCategoryEntity> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Collection<VehicleSubCategoryEntity> subCategories) {
		this.subCategories = subCategories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}
