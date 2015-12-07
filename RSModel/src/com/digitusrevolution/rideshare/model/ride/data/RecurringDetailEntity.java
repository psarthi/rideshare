package com.digitusrevolution.rideshare.model.ride.data;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="recurring_detail")
public class RecurringDetailEntity {
	
	@Id
	@GeneratedValue
	private int id;
	private ZonedDateTime startDate;
	private ZonedDateTime endDate;
	private String repeatFrequency;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
