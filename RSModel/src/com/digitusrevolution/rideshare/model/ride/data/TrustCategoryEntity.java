package com.digitusrevolution.rideshare.model.ride.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;

@Entity
@Table(name="trust_category")
public class TrustCategoryEntity {
	
	@Id
	@Column
	@Enumerated(EnumType.STRING)
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
		if (!(obj instanceof TrustCategoryEntity)) {
			return false;
		}
		TrustCategoryEntity other = (TrustCategoryEntity) obj;
		if (name != other.name) {
			return false;
		}
		return true;
	}
	

}
