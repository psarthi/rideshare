package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class Ride implements DomainModel{

	private RideEntity entity = new RideEntity();
	//id data type needs to be finalized later, whether to use int, long, string
	private int id;
	private ZonedDateTime startTime;
	private RidePoint startPoint = new RidePoint();
	private RidePoint endPoint = new RidePoint();
	private int seatOffered;
	private int luggageCapacityOffered;
	private Sex sexPreference;
	private TrustNetwork trustNetwork = new TrustNetwork();
	private Route route = new Route();
	private boolean recur;
	private RecurringDetail recurringDetail = new RecurringDetail();
	private RideStatus status;
	private RideSeatStatus seatStatus;
	private Vehicle vehicle = new Vehicle();
	private User driver = new User(); 
	private Collection<RidePassenger> ridePassengers = new HashSet<RidePassenger>();
	private Collection<Bill> bills = new HashSet<Bill>();
	private Collection<RideRequest> acceptedRideRequests = new HashSet<RideRequest>();
	private Collection<RideRequest> rejectedRideRequests = new HashSet<RideRequest>();
	private Collection<RideRequest> cancelledRideRequests = new HashSet<RideRequest>();
	private int travelDistance;

	public int getId() {
		return id; 
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public ZonedDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
		entity.setStartTime(startTime);
	}
	public RidePoint getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(RidePoint startPoint) {
		//We are storing only id in entity model
		this.startPoint = startPoint;
		entity.setStartPointId(startPoint.get_id());
	}
	public int getSeatOffered() {
		return seatOffered;
	}
	public void setSeatOffered(int seatOffered) {
		this.seatOffered = seatOffered;
		entity.setSeatOffered(seatOffered);
	}
	public int getLuggageCapacityOffered() {
		return luggageCapacityOffered;
	}
	public void setLuggageCapacityOffered(int luggageCapacityOffered) {
		this.luggageCapacityOffered = luggageCapacityOffered;
		entity.setLuggageCapacityOffered(luggageCapacityOffered);
	}
	public boolean getRecur() {
		return recur;
	}
	public void setRecur(boolean recur) {
		this.recur = recur;
		entity.setRecur(recur);
	}
	public RecurringDetail getRecurringDetail() {
		recurringDetail.setEntity(entity.getRecurringDetail());
		return recurringDetail;
	}
	public void setRecurringDetail(RecurringDetail recurringDetail) {
		this.recurringDetail = recurringDetail;
		entity.setRecurringDetail(recurringDetail.getEntity());
	}
	public RideStatus getStatus() {
		return status;
	}
	public void setStatus(RideStatus status) {
		this.status = status;
		entity.setStatus(status);
	}
	public TrustNetwork getTrustNetwork() {
		trustNetwork.setEntity(entity.getTrustNetwork());
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
		entity.setTrustNetwork(trustNetwork.getEntity());
	}
	public Route getRoute() {
		//Nothing required as we are not storing anything in hibernate
		return route;
	}
	public void setRoute(Route route) {
		//Nothing required as we are not storing anything in hibernate
		this.route = route;
	}
	public Vehicle getVehicle() {
		vehicle.setEntity(entity.getVehicle());
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		entity.setVehicle(vehicle.getEntity());
	}
	public RidePoint getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(RidePoint endPoint) {
		this.endPoint = endPoint;
		//We are storing only id in the hibernate db
		entity.setEndPointId(endPoint.get_id());
	}
	public Collection<Bill> getBills() {
		if (bills.isEmpty()){
			Collection<BillEntity> billEntities = entity.getBills();
			for (BillEntity billEntity : billEntities) {
				Bill bill = new Bill();
				bill.setEntity(billEntity);
				bills.add(bill);
			}
		}
		return bills;
	}
	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
		entity.getBills().clear();
		for (Bill bill : bills) {
			entity.getBills().add(bill.getEntity());
		}
	}

	public Collection<RideRequest> getAcceptedRideRequests() {
		if (acceptedRideRequests.isEmpty()){
			Collection<RideRequestEntity> acceptedRideRequestEntities = entity.getAcceptedRideRequests();
			for (RideRequestEntity rideRequestEntity : acceptedRideRequestEntities) {
				RideRequest rideRequest = new RideRequest();
				rideRequest.setEntity(rideRequestEntity);
				acceptedRideRequests.add(rideRequest);
			}
		}
		return acceptedRideRequests;
	}
	public void setAcceptedRideRequests(Collection<RideRequest> acceptedRideRequests) {
		this.acceptedRideRequests = acceptedRideRequests;
		entity.getAcceptedRideRequests().clear();
		for (RideRequest rideRequest : acceptedRideRequests) {
			entity.getAcceptedRideRequests().add(rideRequest.getEntity());
		}
	}

	public void addAcceptedRideRequest(RideRequest acceptedRideRequest){
		acceptedRideRequests.add(acceptedRideRequest);
		entity.getAcceptedRideRequests().add(acceptedRideRequest.getEntity());
	}

	public Collection<RideRequest> getRejectedRideRequests() {
		if (rejectedRideRequests.isEmpty()){
			Collection<RideRequestEntity> rejectedRideRequestEntities = entity.getRejectedRideRequests();
			for (RideRequestEntity rideRequestEntity : rejectedRideRequestEntities) {
				RideRequest rideRequest = new RideRequest();
				rideRequest.setEntity(rideRequestEntity);
				rejectedRideRequests.add(rideRequest);
			}
		}
		return rejectedRideRequests;
	}
	public void setRejectedRideRequests(Collection<RideRequest> rejectedRideRequests) {
		this.rejectedRideRequests = rejectedRideRequests;
		entity.getRejectedRideRequests().clear();
		for (RideRequest rideRequest : rejectedRideRequests) {
			entity.getRejectedRideRequests().add(rideRequest.getEntity());
		}
	}

	public void addRejectedRideRequest(RideRequest rejectedRideRequest) {
		rejectedRideRequests.add(rejectedRideRequest);
		entity.getRejectedRideRequests().add(rejectedRideRequest.getEntity());
	}

	public User getDriver() {
		driver.setEntity(entity.getDriver());
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
		entity.setDriver(driver.getEntity());
	}
	public Collection<RidePassenger> getRidePassengers() {
		if (ridePassengers.isEmpty()){
			Collection<RidePassengerEntity> ridePassengerEntities = entity.getRidePassengers();
			for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
				RidePassenger ridePassenger = new RidePassenger();
				ridePassenger.setEntity(ridePassengerEntity);
				ridePassengers.add(ridePassenger);
			}
		}
		return ridePassengers;
	}
	public void setRidePassengers(Collection<RidePassenger> ridePassengers) {
		this.ridePassengers = ridePassengers;
		entity.getRidePassengers().clear();
		for (RidePassenger ridePassenger : ridePassengers) {
			entity.getRidePassengers().add(ridePassenger.getEntity());
		}
	}

	public void addRidePassenger(RidePassenger ridePassenger) {
		ridePassengers.add(ridePassenger);
		entity.getRidePassengers().add(ridePassenger.getEntity());
	}

	public Sex getSexPreference() {
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
		entity.setSexPreference(sexPreference);
	}
	public int getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(int travelDistance) {
		this.travelDistance = travelDistance;
		entity.setTravelDistance(travelDistance);
	}
	public Collection<RideRequest> getCancelledRideRequests() {
		if (cancelledRideRequests.isEmpty()){
			Collection<RideRequestEntity> cancelledRideRequestEntities = entity.getCancelledRideRequests();
			for (RideRequestEntity rideRequestEntity : cancelledRideRequestEntities) {
				RideRequest rideRequest = new RideRequest();
				rideRequest.setEntity(rideRequestEntity);
				cancelledRideRequests.add(rideRequest);
			}
		}
		return cancelledRideRequests;
	}
	public void setCancelledRideRequests(Collection<RideRequest> cancelledRideRequests) {
		this.cancelledRideRequests = cancelledRideRequests;
		entity.getCancelledRideRequests().clear();
		for (RideRequest rideRequest : cancelledRideRequests) {
			entity.getCancelledRideRequests().add(rideRequest.getEntity());
		}
	}
	
	public void addCancelledRideRequest(RideRequest cancelledRideRequest) {
		cancelledRideRequests.add(cancelledRideRequest);
		entity.getCancelledRideRequests().add(cancelledRideRequest.getEntity());
	}
	
	public RideSeatStatus getSeatStatus() {
		return seatStatus;
	}
	public void setSeatStatus(RideSeatStatus seatStatus) {
		this.seatStatus = seatStatus;
		entity.setSeatStatus(seatStatus);
	}
	public RideRequest getRideRequestOfPassenger(int passengerId){
		Collection<RideRequest> acceptedRideRequests = getAcceptedRideRequests();
		for (RideRequest rideRequest : acceptedRideRequests) {
			if (rideRequest.getPassenger().getId() == passengerId){
				return rideRequest;
			}
		}
		throw new RuntimeException("Ride Request not found for passenger id:"+passengerId);
	}
	public RidePassenger getRidePassenger(int passengerId){
		Collection<RidePassenger> passengers = getRidePassengers();
		for (RidePassenger ridePassenger : passengers) {
			if (ridePassenger.getPassenger().getId() == passengerId){
				return ridePassenger;
			}
		}
		throw new RuntimeException("No passenger found with id:"+passengerId);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + id;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((vehicle == null) ? 0 : vehicle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ride)) {
			return false;
		}
		Ride other = (Ride) obj;
		if (driver == null) {
			if (other.driver != null) {
				return false;
			}
		} else if (!driver.equals(other.driver)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (startTime == null) {
			if (other.startTime != null) {
				return false;
			}
		} else if (!startTime.equals(other.startTime)) {
			return false;
		}
		if (vehicle == null) {
			if (other.vehicle != null) {
				return false;
			}
		} else if (!vehicle.equals(other.vehicle)) {
			return false;
		}
		return true;
	}
	public RideEntity getEntity() {
		return entity;
	}
	public void setEntity(RideEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}
	@Override
	public void setDomainModelPrimitiveVariable() {
		id = entity.getId();
		luggageCapacityOffered = entity.getLuggageCapacityOffered();
		recur = entity.getRecur();
		seatOffered = entity.getSeatOffered();
		seatStatus = entity.getSeatStatus();
		sexPreference = entity.getSexPreference();
		startTime = entity.getStartTime();
		status = entity.getStatus();
		travelDistance = entity.getTravelDistance();
		//We will set only start point from entity and rest of the details needs to be set in DO
		//By fetching data from mongodb
		startPoint.set_id(entity.getStartPointId());
		//We are storing only id in the hibernate db
		//rest of the properties needs to be set in DO
		endPoint.set_id(entity.getEndPointId());

	}
	@Override
	public void fetchReferenceVariable() {
		// TODO Auto-generated method stub
		
	}


}
