package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.Map;

public class Route {
	
	private int id;
	//Integer will hold sequence of points
	private Map<Integer,Point> points;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Integer,Point> getPoints() {
		return points;
	}
	public void setPoints(Map<Integer,Point> points) {
		this.points = points;
	}

}
