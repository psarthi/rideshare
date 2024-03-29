package com.digitusrevolution.rideshare.common.util;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.TransferHandler.TransferSupport;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.UriBuilder;

import com.digitusrevolution.rideshare.common.service.NotificationService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmGratificationRequest;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmGratificationResponse;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmGratificationStatusRequest;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmGratificationStatusResponse;
import com.digitusrevolution.rideshare.model.billing.dto.paytm.PaytmTransactionStatus;
import com.digitusrevolution.rideshare.model.common.NotificationMessage;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.ride.domain.RideType;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.UserRidesDurationInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.OTPProviderResponse;
import com.digitusrevolution.rideshare.model.user.dto.UserFeedbackInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 
 * This Utility class will do all ground work to call REST services
 * and convert the response to appropriate model
 *
 */
public class RESTClientUtil {

	private static final Logger logger = LogManager.getLogger(NotificationService.class.getName());

	//Note - We are using -1 as user id so that we can create dummy token for internal use
	private static final long systemId = Long.valueOf(PropertyReader.getInstance().getProperty("SYSTEM_INTERNAL_USER_ID"));

	public static User getUser(long id){

		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		User user = response.readEntity(User.class);
		return user;
	}

	public static User getBasicUser(long id){

		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_BASIC_USER_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		User user = response.readEntity(User.class);
		return user;
	}
	
	public static Collection<User> getAllUsers(){

		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_ALL_USER_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		Response response = restClientUtil.get(uri);		
		Collection<User> users = response.readEntity(new GenericType<Collection<User>>() {});
		return users;
	}



	public static Collection<Role> getRoles(long id){

		RESTClientImpl<Role> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_ROLE_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);		
		Collection<Role> roles = response.readEntity(new GenericType<Collection<Role>>() {});
		return roles;
	}

	public static List<GroupDetail> getGroups(long id){

		RESTClientImpl<Role> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_GROUP_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);		
		List<GroupDetail> groups = response.readEntity(new GenericType<List<GroupDetail>>() {});
		return groups;
	}


	public static Vehicle getVehicle(long userId, long vehicleId){

		RESTClientImpl<Vehicle> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VEHICLE_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(userId),Long.toString(vehicleId));
		Response response = restClientUtil.get(uri);
		Vehicle vehicle = response.readEntity(Vehicle.class);
		return vehicle;
	}

	public static String getGeocode(String address){

		RESTClientImpl<String> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_GOOGLE_GEOCODE_URL");
		String key = PropertyReader.getInstance().getProperty("GOOGLE_API_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(address,key);
		Response response = restClientUtil.get(uri);
		String json = response.readEntity(String.class);
		return json;
	}

	public static String getReverserGeocode(Double lat, Double lng){

		RESTClientImpl<String> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_GOOGLE_REVERSE_GEOCODE_URL");
		String key = PropertyReader.getInstance().getProperty("GOOGLE_API_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(lat,lng,key);
		Response response = restClientUtil.get(uri);
		String json = response.readEntity(String.class);
		return json;
	}


	public static String getDirection(Double originLat, Double originLng, Double destinationLat, Double destinationLng, ZonedDateTime departureTimeUTC){

		long departureEpochSecond = departureTimeUTC.toEpochSecond(); 
		RESTClientImpl<String> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_GOOGLE_DIRECTION_URL");
		String key = PropertyReader.getInstance().getProperty("GOOGLE_API_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(originLat,originLng,destinationLat,destinationLng,departureEpochSecond,key);
		Response response = restClientUtil.get(uri);
		String json = response.readEntity(String.class);
		return json;
	}

	public static String getDistance(Double originLat, Double originLng, Double destinationLat, Double destinationLng, ZonedDateTime departureTimeUTC){

		long departureEpochSecond = departureTimeUTC.toEpochSecond(); 
		RESTClientImpl<String> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_GOOGLE_DISTANCE_URL");
		String key = PropertyReader.getInstance().getProperty("GOOGLE_API_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(originLat,originLng,destinationLat,destinationLng,departureEpochSecond,key);
		Response response = restClientUtil.get(uri);
		String json = response.readEntity(String.class);
		return json;
	}

	public static VehicleCategory getVehicleCategory(int id){

		RESTClientImpl<VehicleCategory> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VEHICLE_CATEGORY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		VehicleCategory vehicleCategory = response.readEntity(VehicleCategory.class);
		return vehicleCategory;
	}

	public static VehicleSubCategory getVehicleSubCategory(int id){

		RESTClientImpl<VehicleCategory> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VEHICLE_SUB_CATEGORY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		VehicleSubCategory vehicleSubCategory = response.readEntity(VehicleSubCategory.class);
		return vehicleSubCategory;
	}


	public static Account getVirtualAccount(long number){

		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VIRTUAL_ACCOUNT_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(number));
		Response response = restClientUtil.get(uri);
		Account account= response.readEntity(Account.class);
		return account;
	}

	public static Currency getCurrency(int id){

		RESTClientImpl<Currency> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_CURRENCY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		Currency currency= response.readEntity(Currency.class);
		return currency;
	}

	public static Collection<Country> getCountries(){

		RESTClientImpl<Country> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_COUNTRIES_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		Response response = restClientUtil.get(uri);
		Collection<Country> countries = response.readEntity(new GenericType<Collection<Country>>() {});
		return countries;
	}

	public static Company getCompany(int id){

		RESTClientImpl<Currency> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_COMPANY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		Company company= response.readEntity(Company.class);
		return company;
	}
	
	public static Company getBasicCompany(int id){

		RESTClientImpl<Currency> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_BASIC_COMPANY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		Company company= response.readEntity(Company.class);
		return company;
	}
	
	public static Offer getOffer(int id){

		RESTClientImpl<Currency> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_OFFER_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(id));
		Response response = restClientUtil.get(uri);
		Offer offer= response.readEntity(Offer.class);
		return offer;
	}

	public static Account createVirtualAccount(){

		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("CREATE_VIRTUAL_ACCOUNT_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Account virtualAccount = response.readEntity(Account.class);
			return virtualAccount;
		} else {
			return null;
		}
	}

	public static FullRide getCurrentRide(long driverId){

		RESTClientImpl<Ride> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_CURRENT_RIDE");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(driverId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			return null;
		} 
		FullRide ride= response.readEntity(FullRide.class);
		return ride;
	}

	public static FullRideRequest getCurrentRideRequest(long passengerId){

		RESTClientImpl<RideRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_CURRENT_RIDE_REQUEST");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(passengerId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			return null;
		} 
		FullRideRequest rideRequest= response.readEntity(FullRideRequest.class);
		return rideRequest;
	}

	public static boolean makePayment(Ride ride){
		RESTClientImpl<Ride> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("BILL_PAYMENT_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		//We are passing systemId as its an internal call and we need to pass the id in URL
		URI uri = uriBuilder.build(Long.toString(systemId));
		Response response = restClientUtil.post(uri, ride);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} 
		return false;
	}

	public static Account addMoneyToWallet(long accountNumber, float amount){
		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("ADD_MONEY_TO_WALLET");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(systemId), Long.toString(accountNumber), Float.toString(amount));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Account account= response.readEntity(Account.class);
			return account;
		} 
		return null;
	}
	
	public static Transaction addRewardToWallet(long userId, long accountNumber, float amount, int rewardReimbursementTransactionId){
		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("ADD_REWARD_TO_WALLET");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(userId), Long.toString(accountNumber), Float.toString(amount), Integer.toString(rewardReimbursementTransactionId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Transaction transaction= response.readEntity(Transaction.class);
			return transaction;
		} 
		return null;
	}
	
	public static Transaction getTransaction(long transactionId){
		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_WALLET_TRANSACTION");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(transactionId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Transaction transaction= response.readEntity(Transaction.class);
			return transaction;
		} 
		return null;
	}


	public static boolean userFeedback(long userId, UserFeedbackInfo userFeedbackInfo, RideType rideType){
		RESTClientImpl<UserFeedbackInfo> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("POST_USER_FEEDBACK");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(userId), rideType.toString());

		Response response = restClientUtil.post(uri, userFeedbackInfo);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} 
		return false;
	}

	public static List<Bill> getPendingBills(BasicUser passenger){
		RESTClientImpl<BasicUser> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_PENDING_BILLS");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(systemId));
		Response response = restClientUtil.post(uri, passenger);
		List<Bill> bills = response.readEntity(new GenericType<List<Bill>>() {});
		return bills;
	}

	public static Ride getRide(long rideId) {
		RESTClientImpl<Ride> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_RIDE");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(rideId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Ride ride= response.readEntity(Ride.class);
			return ride;
		} 
		return null;
	}

	public static RideRequest getRideRequest(long rideRequestId) {
		RESTClientImpl<RideRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_RIDE_REQUEST");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Long.toString(rideRequestId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			RideRequest rideRequest = response.readEntity(RideRequest.class);
			return rideRequest;
		} 
		return null;
	}

	public static OTPProviderResponse sendOTP(String mobile, String otp){
		RESTClientImpl<BasicUser> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("SEND_OTP");
		String authkey = PropertyReader.getInstance().getProperty("OTP_AUTH_KEY");
		String senderId = PropertyReader.getInstance().getProperty("OTP_SENDER_ID");
		String otp_expiry_mins = PropertyReader.getInstance().getProperty("OTP_EXPIRY_TIME_IN_MINS");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(authkey, senderId, mobile, otp, otp_expiry_mins);
		Response response = restClientUtil.post(uri, null);
		//Don't readEntity and try to store in OTPResponse as media type is not application/json
		//but the response media content type is text/html, so you can read only as String
		String otpResponseString = response.readEntity(String.class);
		//Don't use JsonObjectMapper to convert String to OTPResponse as both types are different
		//so use below way to get POJO from response string
		JSONUtil<OTPProviderResponse> jsonUtil = new JSONUtil<>(OTPProviderResponse.class);
		OTPProviderResponse otpResponse = jsonUtil.getModel(otpResponseString);
		return otpResponse;
	}

	public static OTPProviderResponse getOTPOnCall(String mobile){
		RESTClientImpl<BasicUser> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_OTP_ON_CALL");
		String authkey = PropertyReader.getInstance().getProperty("OTP_AUTH_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(authkey, mobile);
		Response response = restClientUtil.post(uri, null);
		//Don't readEntity and try to store in OTPResponse as media type is not application/json
		//but the response media content type is text/html, so you can read only as String
		String otpResponseString = response.readEntity(String.class);
		//Don't use JsonObjectMapper to convert String to OTPResponse as both types are different
		//so use below way to get POJO from response string
		JSONUtil<OTPProviderResponse> jsonUtil = new JSONUtil<>(OTPProviderResponse.class);
		OTPProviderResponse otpResponse = jsonUtil.getModel(otpResponseString);
		return otpResponse;
	}

	public static boolean sendNotification(NotificationMessage notificationMessage){
		RESTClientImpl<NotificationMessage> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("FIREBASE_SEND_MESSAGE");
		String key = PropertyReader.getInstance().getProperty("FIREBASE_SERVER_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		headers.add("Authorization", "key="+key);
		Response response = restClientUtil.post(uri, notificationMessage, headers);
		String responseString = response.readEntity(String.class);
		logger.debug("Response:"+responseString);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} else {
			return false;
		}
	}

	public static PaytmTransactionStatus getFinancialTransactionStatus(Map<String, String> paramMap) {
		String json="";
		try {
			json = JsonObjectMapper.getMapper().writeValueAsString(paramMap);
			logger.debug("Json:"+json);
		} catch (JsonProcessingException e) {
			throw new WebApplicationException("Unable to convert to Json");
		}
		RESTClientImpl<RideRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_PAYTM_TRANSACTION_STATUS");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(json);
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			//Don't readEntity and try to store in PaytmTransactionStatus as media type is not application/json
			//but the response media content type is text/html, so you can read only as String
			String responseString = response.readEntity(String.class);
			logger.debug("Response:"+responseString);
			//Don't use JsonObjectMapper to convert String to PaytmTransactionStatus as both types are different
			//so use below way to get POJO from response string
			JSONUtil<PaytmTransactionStatus> jsonUtil = new JSONUtil<>(PaytmTransactionStatus.class);
			PaytmTransactionStatus status = jsonUtil.getModel(responseString);
			return status;
		} 
		return null;
	}
	
	public static PaytmGratificationResponse postPaytmGratificationRequest(PaytmGratificationRequest gratificationRequest, String checksum) {
		JSONUtil<PaytmGratificationRequest> jsonUtilRequest = new JSONUtil<>(PaytmGratificationRequest.class);
		
		RESTClientImpl<PaytmGratificationRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("POST_PAYTM_GRATIFICATION_REQUEST_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		headers.add("mid", gratificationRequest.getRequest().getMerchantGuid());
		headers.add("checksumhash", checksum);
		//IMP - When we are trying to set this, jersey throw warning. So removed it and its working fine for now
		//headers.add("Content-Length", jsonUtilRequest.getJson(gratificationRequest).getBytes().length);
		
		Response response = restClientUtil.post(uri, gratificationRequest, headers);
		String responseString = response.readEntity(String.class);
		logger.debug("Response:"+responseString);	
		JSONUtil<PaytmGratificationResponse> jsonUtil = new JSONUtil<>(PaytmGratificationResponse.class);
		PaytmGratificationResponse paytmGratificationResponse = jsonUtil.getModel(responseString);
		return paytmGratificationResponse;
	}
	
	public static PaytmGratificationStatusResponse getPaytmGratificationRequestStatus(PaytmGratificationStatusRequest gratificationStatusRequest, String checksum) {

		RESTClientImpl<PaytmGratificationStatusRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("POST_PAYTM_GRATIFICATION_CHECK_STATUS_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		headers.add("mid", gratificationStatusRequest.getRequest().getMerchantGuid());
		headers.add("checksumhash", checksum);
		Response response = restClientUtil.post(uri, gratificationStatusRequest, headers);
		String responseString = response.readEntity(String.class);
		logger.debug("Response:"+responseString);
		JSONUtil<PaytmGratificationStatusResponse> jsonUtil = new JSONUtil<>(PaytmGratificationStatusResponse.class);
		PaytmGratificationStatusResponse paytmGratificationStatusResponse = jsonUtil.getModel(responseString);	
		return paytmGratificationStatusResponse;		
	}
	
	public static int getRidesAndRidesRequestsCombinedCount(UserRidesDurationInfo durationInfo){
		RESTClientImpl<UserRidesDurationInfo> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_RIDE_AND_RIDE_REQUEST_COMBINED_COUNT");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		//We are passing systemId as its an internal call and we need to pass the id in URL
		URI uri = uriBuilder.build(Long.toString(systemId));
		Response response = restClientUtil.post(uri, durationInfo);
		Integer count = response.readEntity(Integer.class);
		return count;
	}
	
	public static int getRideRequestsCount(UserRidesDurationInfo durationInfo){
		RESTClientImpl<UserRidesDurationInfo> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_RIDE_REQUEST_COUNT");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		//We are passing systemId as its an internal call and we need to pass the id in URL
		URI uri = uriBuilder.build(Long.toString(systemId));
		Response response = restClientUtil.post(uri, durationInfo);
		Integer count = response.readEntity(Integer.class);
		return count;
	}


}



































