package com.digitusrevolution.rideshare.common.util;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Collection;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.dto.RideDTO;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

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
	
	public static Account getAccount(int number){

		RESTClientImpl<Account> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("GET_ACCOUNT_URL");
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
	
	public static Response generateBill(RideDTO rideDTO){

		RESTClientImpl<RideDTO> restClientUtil = new RESTClientImpl<>();
		String url = PropertyReader.getInstance().getProperty("POST_GENERATE_BILL_URL");
		UriBuilder uriBuilder = UriBuilder.fromUri(url);
		URI uri = uriBuilder.build();
		Response response = restClientUtil.post(uri, rideDTO);
		return response;
	}

}



































