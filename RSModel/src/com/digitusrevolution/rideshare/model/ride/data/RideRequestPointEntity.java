package com.digitusrevolution.rideshare.model.ride.data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="riderequestpoint")
public class RideRequestPointEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int _id;
	@Embedded
	private PointEntity point = new PointEntity();
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public PointEntity getPoint() {
		return point;
	}
	public void setPoint(PointEntity point) {
		this.point = point;
	}

}
