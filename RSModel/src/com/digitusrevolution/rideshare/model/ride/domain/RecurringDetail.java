package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.ride.data.RecurringDetailEntity;

public class RecurringDetail implements DomainModel{
	
	private RecurringDetailEntity entity = new RecurringDetailEntity();
	private int id;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String repeatFrequency;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(ZonedDateTime startDate) {
		this.startDate = startDate;
		entity.setStartDate(startDate);
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(ZonedDateTime endDate) {
		this.endDate = endDate;
		entity.setEndDate(endDate);
	}

	public String getRepeatFrequency() {
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
		setDomainModelPrimitiveVariable();
	}

	@Override
	public void setDomainModelPrimitiveVariable() {
		id = entity.getId();
		startDate = entity.getStartDate();
		endDate = entity.getEndDate();
		repeatFrequency = entity.getRepeatFrequency();
		
		
	}

}
