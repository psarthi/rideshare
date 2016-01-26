package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.ride.data.RecurringDetailEntity;

public class RecurringDetail{
	
	private RecurringDetailEntity entity = new RecurringDetailEntity();
	private int id;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String repeatFrequency;

	public int getId() {
		id = entity.getId();
		return id;
	}

	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}

	public ZonedDateTime getStartDate() {
		startDate = entity.getStartDate();
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
		entity.setStartDate(startDate);
	}

	public ZonedDateTime getEndDate() {
		endDate = entity.getEndDate();
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
		entity.setEndDate(endDate);
	}

	public String getRepeatFrequency() {
		repeatFrequency = entity.getRepeatFrequency();
		return repeatFrequency;
	}

	public void setRepeatFrequency(String repeatFrequency) {
		this.repeatFrequency = repeatFrequency;
		entity.setRepeatFrequency(repeatFrequency);
	}

	public RecurringDetailEntity getEntity() {
		return entity;
	}

	public void setEntity(RecurringDetailEntity entity) {
		this.entity = entity;
	}

}
