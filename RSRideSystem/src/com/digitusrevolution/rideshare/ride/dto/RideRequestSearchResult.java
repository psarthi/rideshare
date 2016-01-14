package com.digitusrevolution.rideshare.ride.dto;

import java.util.List;
import org.geojson.MultiPolygon;

public class RideRequestSearchResult {
	
	private List<RideMatchInfo> rideMatchInfos;
	private double searchDistance;
	private int resultLastIndex;
	private MultiPolygon multiPolygon;  
	
	public List<RideMatchInfo> getRideMatchInfos() {
		return rideMatchInfos;
	}
	public void setRideMatchInfos(List<RideMatchInfo> rideMatchInfos) {
		this.rideMatchInfos = rideMatchInfos;
	}
	public double getSearchDistance() {
		return searchDistance;
	}
	public void setSearchDistance(double searchDistance) {
		this.searchDistance = searchDistance;
	}
	public int getResultLastIndex() {
		return resultLastIndex;
	}
	public void setResultLastIndex(int resultLastIndex) {
		this.resultLastIndex = resultLastIndex;
	}
	public MultiPolygon getMultiPolygon() {
		return multiPolygon;
	}
	public void setMultiPolygon(MultiPolygon multiPolygon) {
		this.multiPolygon = multiPolygon;
	}
	
	

}
