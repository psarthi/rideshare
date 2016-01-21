package com.digitusrevolution.rideshare.model.ride.domain;

public class TrustCategory {
	
	private TrustCategoryName name;

	public TrustCategoryName getName() {
		return name;
	}
	public void setName(TrustCategoryName name) {
		this.name = name;
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
