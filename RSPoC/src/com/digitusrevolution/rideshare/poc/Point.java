package com.digitusrevolution.rideshare.poc;

import java.util.ArrayList;
import java.util.List;

public class Point {
	
	private String type;
	private List<Double> cordinates = new ArrayList<>();
	
	public Point() {
		
	}
	
	public Point(double longitude, double latitude){
		this.type = "Point";
		this.cordinates.add(longitude);
		this.cordinates.add(latitude);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Double> getCordinates() {
		return cordinates;
	}
	public void setCordinates(List<Double> cordinates) {
		this.cordinates = cordinates;
	}
	
	

}
