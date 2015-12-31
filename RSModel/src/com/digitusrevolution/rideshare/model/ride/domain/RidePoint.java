package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RidePoint{
	
	private String _id;
	//Don't change this to HashMap as serialization / deserialization to JSON would be a problem as 
	//Jackson by default understand only basic datatypes as key and not custom object
	@JsonProperty("rides")
	private List<RideBasicInfo> ridesBasicInfo = new ArrayList<RideBasicInfo>();
	private Point point = new Point();
	private int sequence;
	
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
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}		
	public List<RideBasicInfo> getRidesBasicInfo() {
		return ridesBasicInfo;
	}
	public void setRidesBasicInfo(List<RideBasicInfo> ridesBasicInfo) {
		this.ridesBasicInfo = ridesBasicInfo;
	}
	@Override
	public String toString(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
		String rides = "";
		for (RideBasicInfo rideBasicInfo : this.ridesBasicInfo) {
			rides += rideBasicInfo.getId()+","+rideBasicInfo.getDateTime().format(formatter) +":";
		}
		return "[_id:sequence:point:rides]:"+get_id()+":"+getSequence()+":"+getPoint().toString()+":"+rides;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RidePoint){
			RidePoint ridePoint = (RidePoint) obj;
			return this.ridesBasicInfo.equals(ridePoint.ridesBasicInfo);	
		}
		return false;
	}

	/*
	 * Reason of using long instead of int, is to avoid issue in case of sum of rides id goes out of range of int  
	 */
	@Override
	public int hashCode() {
		long sumOfRideHashCode = 0;
		for (RideBasicInfo rideBasicInfo : ridesBasicInfo) {
			sumOfRideHashCode += rideBasicInfo.hashCode();
		}
		return Long.hashCode(sumOfRideHashCode);
	}
}
