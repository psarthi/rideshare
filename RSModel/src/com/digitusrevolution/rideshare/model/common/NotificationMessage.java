package com.digitusrevolution.rideshare.model.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationMessage {

	private String to;
	private ArrayList<String> registration_ids = new ArrayList<String>();
	private Notification notification = new Notification();
	private Map<String, String> data = new HashMap<String, String>();
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public ArrayList<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(ArrayList<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public class Notification {
		private String title;
		private String body;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}		
	}
	
	public enum DataKey{
		message,imageUrl
	}
}
