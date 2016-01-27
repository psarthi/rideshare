package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RideRequest implements DomainModel{

	private RideRequestEntity entity = new RideRequestEntity();
	private int id;
	private RideRequestPoint pickupPoint = new RideRequestPoint();
	private RideRequestPoint dropPoint = new RideRequestPoint();
	private ZonedDateTime pickupTime;
	private LocalTime pickupTimeVariation;
	private VehicleCategory vehicleCategory = new VehicleCategory();
	private VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
	private TrustNetwork trustNetwork = new TrustNetwork();
	private Sex sexPreference;
	private int seatRequired;
	private int luggageCapacityRequired;
	private int pickupPointVariation;
	private int dropPointVariation;
	private RideRequestStatus status;
	private User passenger = new User();
	private boolean ridePreference;
	private Collection<Ride> preferredRides = new HashSet<Ride>();
	private Ride acceptedRide = new Ride();
	private RidePoint ridePickupPoint = new RidePoint();
	private RidePoint rideDropPoint = new RidePoint();
	private int travelTime;
	private int travelDistance;
	private Collection<Ride> cancelledRides = new HashSet<Ride>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public RideRequestPoint getPickupPoint() {
		return pickupPoint;
	}
	public void setPickupPoint(RideRequestPoint pickupPoint) {
		this.pickupPoint = pickupPoint;
		//Set only id as that's the only thing stored in hibernate db
		entity.setPickupPointId(pickupPoint.get_id());
	}
	public RideRequestPoint getDropPoint() {
		return dropPoint;
	}
	public void setDropPoint(RideRequestPoint dropPoint) {
		this.dropPoint = dropPoint;
		//Set only id as that's the only thing stored in hibernate db
		entity.setDropPointId(dropPoint.get_id());
	}
	public ZonedDateTime getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(ZonedDateTime pickupTime) {
		this.pickupTime = pickupTime;
		entity.setPickupTime(pickupTime);
	}
	public LocalTime getPickupTimeVariation() {
		return pickupTimeVariation;
	}
	public void setPickupTimeVariation(LocalTime pickupTimeVariation) {
		this.pickupTimeVariation = pickupTimeVariation;
		entity.setPickupTimeVariation(pickupTimeVariation);
	}
	public VehicleCategory getVehicleCategory() {
		vehicleCategory.setEntity(entity.getVehicleCategory());
		return vehicleCategory;
	}
	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
		entity.setVehicleCategory(vehicleCategory.getEntity());
	}
	public VehicleSubCategory getVehicleSubCategory() {
		vehicleSubCategory.setEntity(entity.getVehicleSubCategory());
		return vehicleSubCategory;
	}
	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
		entity.setVehicleSubCategory(vehicleSubCategory.getEntity());
	}
	public TrustNetwork getTrustNetwork() {
		trustNetwork.setEntity(entity.getTrustNetwork());
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
		entity.setTrustNetwork(trustNetwork.getEntity());
	}
	public int getSeatRequired() {
		return seatRequired;
	}
	public void setSeatRequired(int seatRequired) {
		this.seatRequired = seatRequired;
		entity.setSeatRequired(seatRequired);
	}
	public int getLuggageCapacityRequired() {
		return luggageCapacityRequired;
	}
	public void setLuggageCapacityRequired(int luggageCapacityRequired) {
		this.luggageCapacityRequired = luggageCapacityRequired;
		entity.setLuggageCapacityRequired(luggageCapacityRequired);
	}
	public RideRequestStatus getStatus() {
		return status;
	}
	public void setStatus(RideRequestStatus status) {
		this.status = status;
		entity.setStatus(status);
	}
	public Sex getSexPreference() {
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
		entity.setSexPreference(sexPreference);
	}
	public int getPickupPointVariation() {
		return pickupPointVariation;
	}
	public void setPickupPointVariation(int pickupPointVariation) {
		this.pickupPointVariation = pickupPointVariation;
		entity.setPickupPointVariation(pickupPointVariation);
	}
	public int getDropPointVariation() {
		return dropPointVariation;
	}
	public void setDropPointVariation(int dropPointVariation) {
		this.dropPointVariation = dropPointVariation;
		entity.setDropPointVariation(dropPointVariation);
	}
	public User getPassenger() {
		passenger.setEntity(entity.getPassenger());
		return passenger;
	}
	public void setPassenger(User passenger) {
		this.passenger = passenger;
		entity.setPassenger(passenger.getEntity());
	}
	public Collection<Ride> getPreferredRides() {
		Collection<RideEntity> preferredRideEntities = entity.getPreferredRides();
		for (RideEntity rideEntity : preferredRideEntities) {
			Ride ride = new Ride();
			ride.setEntity(rideEntity);
			preferredRides.add(ride);
		}
		return preferredRides;
	}
	public void setPreferredRides(Collection<Ride> preferredRides) {
		this.preferredRides = preferredRides;
		for (Ride ride : preferredRides) {
			entity.getPreferredRides().add(ride.getEntity());
		}
	}
	public boolean getRidePreference() {
		return ridePreference;
	}
	public void setRidePreference(boolean ridePreference) {
		this.ridePreference = ridePreference;
		entity.setRidePreference(ridePreference);
	}
	public Ride getAcceptedRide() {
		acceptedRide.setEntity(entity.getAcceptedRide());
		return acceptedRide;
	}
	public void setAcceptedRide(Ride acceptedRide) {
		this.acceptedRide = acceptedRide;
		entity.setAcceptedRide(acceptedRide.getEntity());
	}
	public RidePoint getRidePickupPoint() {
		return ridePickupPoint;
	}
	public void setRidePickupPoint(RidePoint ridePickupPoint) {
		this.ridePickupPoint = ridePickupPoint;
		//Only Id is required as that's what is stored in hibernate DB
		entity.setPickupPointId(pickupPoint.get_id());
	}
	public RidePoint getRideDropPoint() {
		return rideDropPoint;
	}
	public void setRideDropPoint(RidePoint rideDropPoint) {
		this.rideDropPoint = rideDropPoint;
		//Only Id is required as that's what is stored in hibernate DB
		entity.setDropPointId(dropPoint.get_id());
	}
	public int getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
		entity.setTravelTime(travelTime);
	}
	public int getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(int travelDistance) {
		this.travelDistance = travelDistance;
		entity.setTravelDistance(travelDistance);
	}
	public Collection<Ride> getCancelledRides() {
		Collection<RideEntity> cancelledRideEntities = entity.getCancelledRides();
		for (RideEntity rideEntity : cancelledRideEntities) {
			Ride ride = new Ride();
			ride.setEntity(rideEntity);
			cancelledRides.add(ride);
		}
		return cancelledRides;
	}
	public void setCancelledRides(Collection<Ride> cancelledRides) {
		this.cancelledRides = cancelledRides;
		for (Ride ride : cancelledRides) {
			entity.getCancelledRides().add(ride.getEntity());
		}
	}
	public RideRequestEntity getEntity() {
		return entity;
	}
	public void setEntity(RideRequestEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((passenger == null) ? 0 : passenger.hashCode());
		result = prime * result + ((pickupTime == null) ? 0 : pickupTime.hashCode());
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
		if (!(obj instanceof RideRequest)) {
			return false;
		}
		RideRequest other = (RideRequest) obj;
		if (id != other.id) {
			return false;
		}
		if (passenger == null) {
			if (other.passenger != null) {
				return false;
			}
		} else if (!passenger.equals(other.passenger)) {
			return false;
		}
		if (pickupTime == null) {
			if (other.pickupTime != null) {
				return false;
			}
		} else if (!pickupTime.equals(other.pickupTime)) {
			return false;
		}
		return true;
	}
	@Override
	public void setDomainModelPrimitiveVariable() {
		//Set only id as that's the only thing stored in hibernate db
		dropPoint.set_id(entity.getDropPointId());
		dropPointVariation = entity.getDropPointVariation();
		id = entity.getId();
		luggageCapacityRequired = entity.getLuggageCapacityRequired();
		//Set only id as that's the only thing stored in hibernate db
		pickupPoint.set_id(entity.getPickupPointId());
		pickupPointVariation = entity.getPickupPointVariation();
		pickupTime = entity.getPickupTime();
		pickupTimeVariation = entity.getPickupTimeVariation();
		//Only Id is required as that's what is stored in hibernate DB
		rideDropPoint.set_id(entity.getDropPointId());
		//Only Id is required as that's what is stored in hibernate DB
		ridePickupPoint.set_id(entity.getPickupPointId());
		ridePreference = entity.getRidePreference();
		seatRequired = entity.getSeatRequired();
		sexPreference = entity.getSexPreference();
		status = entity.getStatus();
		travelDistance = entity.getTravelDistance();
		travelTime = entity.getTravelTime();
		
	}

}
