package com.digitusrevolution.rideshare.model.ride.data;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;


@Embeddable
public class RouteEntity {
	
	@Embedded
	@ElementCollection
	@JoinTable(name="route_point",joinColumns=@JoinColumn(name="ride_id"))
	@OneToMany(cascade=CascadeType.ALL)
	//Need to find way to change column name of value to sequence
	private Map<Integer,PointEntity> points = new HashMap<Integer, PointEntity>();

	public Map<Integer, PointEntity> getPoints() {
		return points;
	}

	public void setPoints(Map<Integer, PointEntity> points) {
		this.points = points;
	}	
	
}
