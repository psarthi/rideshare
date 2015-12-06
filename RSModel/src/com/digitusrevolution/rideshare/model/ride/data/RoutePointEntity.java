package com.digitusrevolution.rideshare.model.ride.data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class RoutePointEntity {
	
	private int sequence;
	@OneToOne(cascade=CascadeType.ALL)
	private PointEntity point;
	
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public PointEntity getPoint() {
		return point;
	}
	public void setPoint(PointEntity point) {
		this.point = point;
	}
	
	

}
