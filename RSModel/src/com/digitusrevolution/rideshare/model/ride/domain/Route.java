package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.Map;

public class Route {
	
	private int id;
	//Integer will hold sequence of points
	private Map<Point,Integer> points;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Map<Point, Integer> getPoints() {
		return points;
	}
	public void setPoints(Map<Point, Integer> points) {
		this.points = points;
	}

}
