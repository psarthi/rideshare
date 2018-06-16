package com.digitusrevolution.rideshare.common.service;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.MathUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.common.util.RSUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.common.NotificationMessage;
import com.digitusrevolution.rideshare.model.ride.domain.CancellationType;
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
		logger.info("Sending Ride Match Notification to Driver:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);

		//Sending Notification to Passenger
		notificationMessage.setTo(rideRequest.getPassenger().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
		notificationMessage.getNotification().setBody("Found matching ride owner "+ride.getDriver().getFirstName());
		logger.info("Sending Ride Match Notification to Passenger:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);	
	}

	public static void sendCancelRideNotification(Ride ride, RideRequest rideRequest, CancellationType cancellationType) {
		NotificationMessage notificationMessage = new NotificationMessage();
		if (cancellationType.equals(CancellationType.Driver) || cancellationType.equals(CancellationType.RideRequest)) {
			//Sending Notification to Driver
			notificationMessage.setTo(ride.getDriver().getPushNotificationToken());
			notificationMessage.getNotification().setTitle("Offered Ride - "+DateTimeUtil.getFormattedDateTimeString(ride.getStartTime()));
			notificationMessage.getNotification().setBody(rideRequest.getPassenger().getFirstName() + " cancelled the ride");
			logger.info("Sending Ride Cancellation Notification to Driver:"+jsonUtil.getJson(notificationMessage));
			RESTClientUtil.sendNotification(notificationMessage);			
		}
		
		if (cancellationType.equals(CancellationType.Passenger) || cancellationType.equals(CancellationType.Ride)) {
			//Sending Notification to Passenger
			notificationMessage.setTo(rideRequest.getPassenger().getPushNotificationToken());
			notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
			notificationMessage.getNotification().setBody(ride.getDriver().getFirstName() + " cancelled the ride");
			logger.info("Sending Ride Cancellation Notification to Passenger:"+jsonUtil.getJson(notificationMessage));
			RESTClientUtil.sendNotification(notificationMessage);			
		}
	}

	public static void sendInsufficientBalanceNotification(RideRequest rideRequest) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(rideRequest.getPassenger().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
		notificationMessage.getNotification().setBody("Insufficient balance, ride match failed");
		logger.info("Sending Insufficient Balance Notification to Passenger:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}

	public static void sendGroupApprovedNotification(Group group, User user) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Group "+group.getName());
		notificationMessage.getNotification().setBody("Membership request Approved");
		logger.info("Sending Membership Request Approved notification:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}
	
	public static void sendGroupMembershipRequestNotification(Group group, User user) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(group.getOwner().getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Group "+group.getName());
		notificationMessage.getNotification().setBody(user.getFirstName()+" has submitted membership request");
		logger.info("Sending New Membership Request notification to owner:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}

	public static void sendGroupRejectNotification(Group group, User user) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Group "+group.getName());
		notificationMessage.getNotification().setBody("Membership request Rejected");
		logger.info("Sending Membership Request Rejected notification:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}

	public static void sendGroupInviteNotification(Group group, User user) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Group "+group.getName());
		notificationMessage.getNotification().setBody("You are invited");
		logger.info("Sending Group Invitation notification:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);		
	}
	
	public static void sendRideOfferAdminNotification(Ride ride) {
		NotificationMessage notificationMessage = new NotificationMessage();
		User admin = RESTClientUtil.getBasicUser(1);
		//Sending Notification to Admin
		notificationMessage.setTo(admin.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Offered Ride - "+DateTimeUtil.getFormattedDateTimeString(ride.getStartTime()));
		notificationMessage.getNotification().setBody("From:"+ride.getStartPointAddress()+"\n"+"To:"+ride.getEndPointAddress());
		logger.info("Sending Offered Ride Notification to Admin:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}
	
	public static void sendRideRequestAdminNotification(RideRequest rideRequest) {
		NotificationMessage notificationMessage = new NotificationMessage();
		User admin = RESTClientUtil.getBasicUser(1);
		//Sending Notification to Admin
		notificationMessage.setTo(admin.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Requested Ride - "+DateTimeUtil.getFormattedDateTimeString(rideRequest.getPickupTime()));
		notificationMessage.getNotification().setBody("From:"+rideRequest.getPickupPointAddress()+"\n"+"To:"+rideRequest.getDropPointAddress());
		//Sample reference here for setting the url for displaying image in notification
		//notificationMessage.getData().put(NotificationMessage.DataKey.imageUrl.toString(), "https://s3.ap-south-1.amazonaws.com/com.parift.rideshare.test.photos/interest/startup.jpg");
		logger.info("Sending Requested Ride Notification to Admin:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}
	
	public static void sendRideMatchAdminNotification(Ride ride, RideRequest rideRequest) {
		NotificationMessage notificationMessage = new NotificationMessage();
		User admin = RESTClientUtil.getBasicUser(1);
		//Sending Notification to Admin
		notificationMessage.setTo(admin.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Matched Ride - "+DateTimeUtil.getFormattedDateTimeString(
				rideRequest.getRidePickupPoint().getRidePointProperties().get(0).getDateTime()));
		notificationMessage.getNotification().setBody("From:"+rideRequest.getRidePickupPointAddress()+"\n"+"To:"+rideRequest.getRideDropPointAddress());
		logger.info("Sending Matched Ride Notification to Admin:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}

	public static void sendUserRegistrationAdminNotification(User newUser) {
		NotificationMessage notificationMessage = new NotificationMessage();
		User admin = RESTClientUtil.getBasicUser(1);
		//Sending Notification to Admin
		notificationMessage.setTo(admin.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("New User registered - "+DateTimeUtil.getFormattedDateTimeString(newUser.getRegistrationDateTime()));
		notificationMessage.getNotification().setBody("User Name - "+newUser.getFirstName()+" "+newUser.getLastName());
		logger.info("Sending New User Registration Notification to Admin:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}
	
	public static void sendDebitNotification(User user, Transaction transaction) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Wallet Debit - "+DateTimeUtil.getFormattedDateTimeString(transaction.getDateTime()));
		notificationMessage.getNotification().setBody(RSUtil.getCurrencySymbol(user.getCountry()) 
				+ MathUtil.getDecimalFormattedString(transaction.getAmount()) +" debited from your wallet. Purpose - "+transaction.getRemark().getPurpose());
		logger.info("Sending Debit Notification to User:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}
	
	public static void sendCreditNotification(User user, Transaction transaction) {
		NotificationMessage notificationMessage = new NotificationMessage();
		notificationMessage.setTo(user.getPushNotificationToken());
		notificationMessage.getNotification().setTitle("Wallet Credit - "+DateTimeUtil.getFormattedDateTimeString(transaction.getDateTime()));
		notificationMessage.getNotification().setBody(RSUtil.getCurrencySymbol(user.getCountry()) 
				+ MathUtil.getDecimalFormattedString(transaction.getAmount()) +" credited to your wallet. Purpose - "+transaction.getRemark().getPurpose());
		logger.info("Sending Credit Notification to User:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}
	
	//This function can be used to broadcast when new offers gets added
	public static void sendNotificationToAllUsers(String title, String body, String imageUrl) {
		NotificationMessage notificationMessage = new NotificationMessage();
		Collection<User> allUsers = RESTClientUtil.getAllUsers();
		for (User user: allUsers) {
			notificationMessage.getRegistration_ids().add(user.getPushNotificationToken());
		}
		//IMP - Its important to set the message value as true, as this is the key which 
		//determines whether to use other data values or not in notification message
		notificationMessage.getData().put(NotificationMessage.DataKey.message.toString(), "true");
		
		/*Note - We need to set the title and body inside the data map as this would be used for building notification with image
		  when app is in bacjground. When app is in foreground, then only notification message with 
		  image and standard title / body field would be used for displaying the noticiation with image
		
		Referece - https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase
		There are two types of messages in FCM (Firebase Cloud Messaging):

		Display Messages: These messages trigger the onMessageReceived() callback only when your app is in foreground
		Data Messages: Theses messages trigger the onMessageReceived() callback even if your app is in foreground/background/killed
		
		Body using topics:
			
			{
			    "to": "/topics/my_topic",
			    "data": {
			        "my_custom_key": "my_custom_value",
			        "my_custom_key2": true
			     }
			}
			Or if you want to send it to specific devices:
			
			{
			    "data": {
			        "my_custom_key": "my_custom_value",
			        "my_custom_key2": true
			     },
			    "registration_ids": ["{device-token}","{device2-token}","{device3-token}"]
			}
		 */
		
		notificationMessage.getData().put("title", title);
		notificationMessage.getData().put("body", body);
		notificationMessage.getData().put(NotificationMessage.DataKey.imageUrl.toString(), imageUrl);
		logger.info("Sending Notification to All User:"+jsonUtil.getJson(notificationMessage));
		RESTClientUtil.sendNotification(notificationMessage);
	}

}
