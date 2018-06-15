package com.digitusrevolution.rideshare.model.ride.dto;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RidesDuration;

public class UserRidesDurationInfo {
	private long userId;
	private ZonedDateTime weekDayDate;
	private RidesDuration ridesDuration;
	private int dailyMaxLimit;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public ZonedDateTime getWeekDayDate() {
		return weekDayDate;
	}
	public void setWeekDayDate(ZonedDateTime weekDayDate) {
		this.weekDayDate = weekDayDate;
	}
	public RidesDuration getRidesDuration() {
		return ridesDuration;
	}
	public void setRidesDuration(RidesDuration ridesDuration) {
		this.ridesDuration = ridesDuration;
	}
	public int getDailyMaxLimit() {
		return dailyMaxLimit;
	}
	public void setDailyMaxLimit(int dailyMaxLimit) {
		this.dailyMaxLimit = dailyMaxLimit;
	}
	
	
}
