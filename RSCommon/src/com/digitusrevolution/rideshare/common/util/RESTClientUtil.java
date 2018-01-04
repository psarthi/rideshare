package com.digitusrevolution.rideshare.common.util;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import javax.swing.TransferHandler.TransferSupport;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.ride.domain.RideType;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.UserFeedbackInfo;

/**
 * 
 * This Utility class will do all ground work to call REST services
 * and convert the response to appropriate model
 *
 */
public class RESTClientUtil {

	public static User getUser(int id){

		RESTClientImpl<User> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(id));
		Response response = restClientUtil.get(uri);
		User user = response.readEntity(User.class);
		return user;
	}

	public static Collection<Role> getRoles(int id){

		RESTClientImpl<Role> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_USER_ROLE_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(id));
		Response response = restClientUtil.get(uri);		
		Collection<Role> roles = response.readEntity(new GenericType<Collection<Role>>() {});
		return roles;
	}

	public static Vehicle getVehicle(int userId, int vehicleId){

		RESTClientImpl<Vehicle> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VEHICLE_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(userId),Integer.toString(vehicleId));
		Response response = restClientUtil.get(uri);
		Vehicle vehicle = response.readEntity(Vehicle.class);
		return vehicle;
	}
	
	public static String getGeocode(String address){

		RESTClientImpl<String> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_GOOGLE_GEOCODE_URL");
		String key = PropertyReader.getInstance().getProperty("GOOGLE_SERVER_KEY");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(address,key);
		Response response = restClientUtil.get(uri);
		String json = response.readEntity(String.class);
		return json;
	}
	
	public static String getReverserGeocode(Double lat, Double lng){

		RESTClientImpl<String> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_GOOGLE_REVERSE_GEOCODE_URL");
		String key = PropertyReader.getInstance().getProperty("GOOGLE_SERVER_KEY");
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
		String key = PropertyReader.getInstance().getProperty("GOOGLE_SERVER_KEY");
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
		String key = PropertyReader.getInstance().getProperty("GOOGLE_SERVER_KEY");
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
		URI uri = uriBuilder.build(Integer.toString(id));
		Response response = restClientUtil.get(uri);
		VehicleCategory vehicleCategory = response.readEntity(VehicleCategory.class);
		return vehicleCategory;
	}
	
	public static VehicleSubCategory getVehicleSubCategory(int id){

		RESTClientImpl<VehicleCategory> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VEHICLE_SUB_CATEGORY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(id));
		Response response = restClientUtil.get(uri);
		VehicleSubCategory vehicleSubCategory = response.readEntity(VehicleSubCategory.class);
		return vehicleSubCategory;
	}

	
	public static Account getVirtualAccount(int number){

		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_VIRTUAL_ACCOUNT_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(number));
		Response response = restClientUtil.get(uri);
		Account account= response.readEntity(Account.class);
		return account;
	}

	public static Currency getCurrency(int id){

		RESTClientImpl<Currency> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_CURRENCY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(id));
		Response response = restClientUtil.get(uri);
		Currency currency= response.readEntity(Currency.class);
		return currency;
	}

	public static Company getCompany(int id){

		RESTClientImpl<Currency> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_COMPANY_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(id));
		Response response = restClientUtil.get(uri);
		Company company= response.readEntity(Company.class);
		return company;
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
	
	public static FullRide getCurrentRide(int driverId){

		RESTClientImpl<Ride> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_CURRENT_RIDE");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(driverId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			return null;
		} 
		FullRide ride= response.readEntity(FullRide.class);
		return ride;
	}
	
	public static FullRideRequest getCurrentRideRequest(int passengerId){

		RESTClientImpl<RideRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_CURRENT_RIDE_REQUEST");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(passengerId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
			return null;
		} 
		FullRideRequest rideRequest= response.readEntity(FullRideRequest.class);
		return rideRequest;
	}
	
	public static boolean makePayment(BillInfo billInfo){
		RESTClientImpl<BillInfo> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("BILL_PAYMENT_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		Response response = restClientUtil.post(uri, billInfo);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} 
		return false;
	}
	
	public static Account addMoneyToWallet(int accountNumber, float amount){
		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("ADD_MONEY_TO_WALLET");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(accountNumber), Float.toString(amount));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Account account= response.readEntity(Account.class);
			return account;
		} 
		return null;
	}
	
	public static boolean userFeedback(int userId, UserFeedbackInfo userFeedbackInfo, RideType rideType){
		RESTClientImpl<UserFeedbackInfo> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("POST_USER_FEEDBACK");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(userId), rideType.toString());
		
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
		URI uri = uriBuilder.build();
		
		Response response = restClientUtil.post(uri, passenger);
		List<Bill> bills = response.readEntity(new GenericType<List<Bill>>() {});
		return bills;
	}
	
	public static Ride getRide(int rideId) {
		RESTClientImpl<Ride> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_RIDE");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(rideId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			Ride ride= response.readEntity(Ride.class);
			return ride;
		} 
		return null;
	}
	
	public static RideRequest getRideRequest(int rideRequestId) {
		RESTClientImpl<RideRequest> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_RIDE_REQUEST");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build(Integer.toString(rideRequestId));
		Response response = restClientUtil.get(uri);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			RideRequest rideRequest = response.readEntity(RideRequest.class);
			return rideRequest;
		} 
		return null;
	}

	
}



































