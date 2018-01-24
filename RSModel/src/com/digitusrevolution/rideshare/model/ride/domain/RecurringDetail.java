package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.ZonedDateTime;

public class RecurringDetail {
	
	private long id;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String repeatFrequency;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
	}

	public String getRepeatFrequency() {
		return repeatFrequency;
	}

	public void setRepeatFrequency(String repeatFrequency) {
		this.repeatFrequency = repeatFrequency;
	}

}
