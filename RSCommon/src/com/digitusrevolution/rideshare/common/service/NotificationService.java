package com.digitusrevolution.rideshare.common.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.common.NotificationMessage;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class NotificationService {
	
	private static final Logger logger = LogManager.getLogger(NotificationService.class.getName());
	private static JSONUtil<NotificationMessage> jsonUtil = new JSONUtil<>(NotificationMessage.class);

	public static void sendMatchedRideNotification(Ride ride, RideRequest rideRequest) {
		NotificationMessage notificationMessage = new NotificationMessage();
		//Sending Notification to Driver
		notificationMessage.setTo(ride.getDriver().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Offered Ride - "+DateTimeUtil.getFormattedDateTimeString(ride.getStartTime()));
		notificationMessage.getNotification().setBody("Found matching coTraveller "+rideRequest.getPassenger().getFirstName());
		logger.debug("Sending Ride Match Notification to Driver:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);

		//Sending Notification to Passenger
		notificationMessage.setTo(rideRequest.getPassenger().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
		notificationMessage.getNotification().setBody("Found matching ride owner "+ride.getDriver().getFirstName());
		logger.debug("Sending Ride Match Notification to Passenger:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);	
	}

	public static void sendCancelRideNotification(Ride ride, RideRequest rideRequest) {
		NotificationMessage notificationMessage = new NotificationMessage();
		//Sending Notification to Driver
		notificationMessage.setTo(ride.getDriver().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Offered Ride - "+DateTimeUtil.getFormattedDateTimeString(ride.getStartTime()));
		notificationMessage.getNotification().setBody(rideRequest.getPassenger().getFirstName() + " cancelled the ride");
		logger.debug("Sending Ride Cancellation Notification to Driver:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
		
		//Sending Notification to Passenger
		notificationMessage.setTo(rideRequest.getPassenger().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
		notificationMessage.getNotification().setBody(ride.getDriver().getFirstName() + " cancelled the ride");
		logger.debug("Sending Ride Cancellation Notification to Passenger:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}

	public static void sendInsufficientBalanceNotification(RideRequest rideRequest) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(rideRequest.getPassenger().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
		notificationMessage.getNotification().setBody("Insufficient balance, ride match failed");
		logger.debug("Sending Insufficient Balance Notification to Passenger:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}

	public static void sendGroupApprovedNotification(Group group, User user) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Group "+group.getName());
		notificationMessage.getNotification().setBody("Membership request Approved");
		logger.debug("Sending Membership Request Approved notification:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}

	public static void sendGroupRejectNotification(Group group, User user) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Group "+group.getName());
		notificationMessage.getNotification().setBody("Membership request Rejected");
		logger.debug("Sending Membership Request Rejected notification:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}


}
