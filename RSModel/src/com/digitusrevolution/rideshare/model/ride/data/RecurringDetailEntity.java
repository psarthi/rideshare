package com.digitusrevolution.rideshare.model.ride.data;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

import com.digitusrevolution.rideshare.model.ride.domain.RecurringStatus;
import com.digitusrevolution.rideshare.model.ride.domain.WeekDay;

@Embeddable
public class RecurringDetailEntity {
	
	@Enumerated(EnumType.STRING)
	private RecurringStatus recurringStatus;
	@ElementCollection(targetClass=WeekDay.class)
	@CollectionTable(name="recurring_days", joinColumns=@JoinColumn(name="ride_id"))
	@Enumerated(EnumType.STRING)
	List<WeekDay> weekDays = new LinkedList<WeekDay>();
	

	public RecurringStatus getRecurringStatus() {
		return recurringStatus;
	}

	public void setRecurringStatus(RecurringStatus recurringStatus) {
		this.recurringStatus = recurringStatus;
	}

	public List<WeekDay> getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(List<WeekDay> weekDays) {
		this.weekDays = weekDays;
	}
	
}
