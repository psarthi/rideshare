package com.digitusrevolution.rideshare.model.ride.domain;

public class Location {
	
	private String _id;
	private Point point = new Point();
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	

}
