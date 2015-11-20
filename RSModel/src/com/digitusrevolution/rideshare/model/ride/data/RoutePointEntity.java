package com.digitusrevolution.rideshare.model.ride.data;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class RoutePointEntity {

	@OneToOne
	private PointEntity point;
	private int sequence;
	
	public PointEntity getPoint() {
		return point;
	}
	public void setPoint(PointEntity point) {
		this.point = point;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
