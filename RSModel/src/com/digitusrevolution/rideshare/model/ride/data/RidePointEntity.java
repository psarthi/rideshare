package com.digitusrevolution.rideshare.model.ride.data;

import java.time.ZonedDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ridepoint")
public class RidePointEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int _id; 
	private int rideId;
	private ZonedDateTime dateTime;
	@Embedded
	private PointEntity point = new PointEntity();
	private int sequence;
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getRideId() {
		return rideId;
	}
	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
	public ZonedDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}
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
