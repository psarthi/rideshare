package com.digitusrevolution.rideshare.model.ride.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="trust_category")
public class TrustCategoryEntity {
	
	@Id
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
