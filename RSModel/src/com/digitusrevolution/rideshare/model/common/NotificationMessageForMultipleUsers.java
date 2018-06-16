package com.digitusrevolution.rideshare.model.common;

import java.util.ArrayList;

public class NotificationMessageForMultipleUsers extends NotificationMessage{

	private ArrayList<String> registration_ids = new ArrayList<String>();

	public ArrayList<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(ArrayList<String> registration_ids) {
		this.registration_ids = registration_ids;
	}
	
}
