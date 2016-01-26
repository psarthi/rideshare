package com.digitusrevolution.rideshare.model.ride.domain;

import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;

public class TrustCategory {
	
	private TrustCategoryEntity entity = new TrustCategoryEntity();
	private TrustCategoryName name;

	public TrustCategoryName getName() {
		name = entity.getName();
		return name;
	}
	public void setName(TrustCategoryName name) {
		this.name = name;
		entity.setName(name);
	}
	public TrustCategoryEntity getEntity() {
		return entity;
	}
	public void setEntity(TrustCategoryEntity entity) {
		this.entity = entity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof TrustCategory)) {
			return false;
		}
		TrustCategory other = (TrustCategory) obj;
		if (name != other.name) {
			return false;
		}
		return true;
	}
	
}
