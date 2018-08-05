package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.LinkedList;
import java.util.List;

public class RecurringDetail {
	
	private RecurringStatus recurringStatus;
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
