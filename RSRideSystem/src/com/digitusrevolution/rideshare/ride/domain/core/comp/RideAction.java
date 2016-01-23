package com.digitusrevolution.rideshare.ride.domain.core.comp;

import java.util.Collection;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.exception.RideRequestUnavailableException;
import com.digitusrevolution.rideshare.common.exception.RideUnavailableException;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideAction {

	private static final Logger logger = LogManager.getLogger(RideAction.class.getName());
	private RideDO rideDO;

	//No need to create empty constructor for Jackson JSON conversion, as these classes would have primarily only behaviors and no state
	public RideAction(RideDO rideDO) {
		this.rideDO = rideDO;
	}

	/*
	 * Purpose - Accept ride request
	 * 
	 * High level logic -
	 * 
	 * - Check status of ride to see if seats are still available
	 * - Check status of ride request to see if its still looking for ride i.e. its unfulfilled
	 * - Check whether seats required is available in the ride as it may be the case that in between partial seats may have been occupied
	 * - If all condition is true, then set the ride as accepted ride in ride request
	 * 		Note - By adding the ride request in the getAcceptedRideRequests collection, it will not update the ride id in the ride request table 
	 * 			   as ride is acceptedRideRequests relationship is owned by ride request entity and not ride (@OneToMany(mappedBy="acceptedRide"))
	 * - Then, add the passenger in the passenger list of ride with initial status of passenger
	 * - Else, throw Ride request unavailable exception
	 * - Update the status of ride request to fulfilled
	 * - Check if total seats occupied including current ride request is equal to seats offered
	 * - If its equal, change the status of ride to fulfilled
	 * 
	 */
	public void acceptRideRequest(int rideId, int rideRequestId){

		RideStatus rideStatus = rideDO.getStatus(rideId);
		Ride ride = rideDO.getChild(rideId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequestStatus rideRequestStatus = rideRequestDO.getStatus(rideRequestId);
		RideRequest rideRequest = rideRequestDO.get(rideRequestId);

		//Check if ride request is unfulfilled
		//Reason for re-checking status criteria as from the time of search to responding to it, 
		//there may be someone else who may have accepted the ride request already and status may have changed
		if (rideRequestStatus.equals(RideRequestStatus.Unfulfilled)){
			//This will check if ride seats are available by checking ride status as unfulfilled
			//Reason for re-checking status criteria as ride may have accepted some other ride request and seats may have got full, when trying to accept
			//another one ride may not be available
			if (rideStatus.equals(RideStatus.Unfulfilled)){
				//Apart from that seats required should be less than or equal to seats offered.
				//Its important to re-check seats criteria as in between it may happen that number of seats which was initially free at the time of search,  
				//partial seats may have been occupied.
				if (rideRequest.getSeatRequired() <= ride.getSeatOffered()){
					//Set accepted ride in ride request
					//Note - By adding the ride request in the getAcceptedRideRequests collection, it will not update the ride id in the ride request table
					//as ride is acceptedRideRequests relationship is owned by ride request entity and not ride (@OneToMany(mappedBy="acceptedRide"))
					rideRequest.setAcceptedRide(ride);
					//Change ride request status to accept status
					rideRequest.setStatus(RideRequestStatus.Fulfilled);
					//Adding passenger
					RidePassenger ridePassenger = new RidePassenger();
					ridePassenger.setPassenger(rideRequest.getPassenger());
					ridePassenger.setStatus(PassengerStatus.Confirmed);
					ride.getPassengers().add(ridePassenger);

					//This should include the current required seat as we need to calculate total seats including this ride request 
					int totalSeatsOccupied = rideRequest.getSeatRequired();
					//Since each ride request may have different seat requirement, so we need to calculate total of all required seats of accepted ride
					//Note - Seats requirement and seat offered criteria should be met while searching for ride or ride requests
					for (RideRequest acceptedRideRequest : ride.getAcceptedRideRequests()) {
						totalSeatsOccupied = totalSeatsOccupied + acceptedRideRequest.getSeatRequired();
					}

					//This will check if seats offered is equal to the accepted ride request including this ride request
					//This will change the status to fulfilled
					if (totalSeatsOccupied == ride.getSeatOffered()){
						ride.setStatus(RideStatus.Fulfilled);
					}

					//Update all the changes in DB for ride and ride request
					rideDO.update(ride);
					//This is required to update accepted ride as well as status update on ride request table
					rideRequestDO.update(rideRequest);
				}
				else{
					throw new RideUnavailableException("Ride doesn't have sufficient seats available with id:"+rideId);
				}			
			}
			else{
				throw new RideUnavailableException("Ride is not available anymore with id:"+rideId);
			}			
		}
		else{
			throw new RideRequestUnavailableException("Ride Request is not available anymore with id:"+rideRequestId);
		}			

	}

	/*
	 * Purpose - Add ride request in the rejected list of ride
	 * 
	 */
	public void rejectRideRequest(int rideId, int rideRequestId){

		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.get(rideRequestId);
		Ride ride = rideDO.get(rideId);

		ride.getRejectedRideRequests().add(rideRequest);	

		//This will update the ride details in DB
		rideDO.update(ride);
	}

	/*
	 * Purpose - Change the ride status to started status
	 * 
	 * High Level Logic -
	 * 
	 * - Check the status and see if its in valid state which is either initial status or fulfilled status 
	 * - If its in valid state, then change the status to started
	 * 
	 */
	public void startRide(int rideId){ 
		Ride ride = rideDO.get(rideId);
		RideStatus rideCurrentStatus = ride.getStatus();
		if (rideCurrentStatus.equals(RideStatus.Unfulfilled) || rideCurrentStatus.equals(RideStatus.Fulfilled)){
			ride.setStatus(RideStatus.Started);
			rideDO.update(ride);			
		} else {
			throw new WebApplicationException("Ride can't be started as its not in valid state. Ride current status:"+rideCurrentStatus);
		}
	}

	/*
	 * Purpose - Update the status of passenger pickup status once picked up
	 * 
	 * High level logic -
	 * 
	 * - Check if the ride is in started state
	 * - Also check if the passenger is in the list itself or not, as it may happen in between passenger may have cancelled the ride request
	 * - If passenger found, then check if passenger is in initial state
	 * - If yes, then change the status of passenger to pickup state
	 * 
	 */
	public void pickupPassenger(int rideId, int passengerId){
		Ride ride = rideDO.getChild(rideId);
		RideStatus rideCurrentStatus = ride.getStatus();
		//Check if ride is started
		if (rideCurrentStatus.equals(RideStatus.Started)){
			Collection<RidePassenger> passengers = ride.getPassengers();
			boolean passengerNotFound = true;
			//Get matching passenger from list of passengers
			RidePassenger passenger = getPassenger(passengers, passengerId);
			//Check if passenger is found
			//As it may happen, that in between passenger has cancelled the ride request, so passenger would not be available 
			if (passenger!=null){
				passengerNotFound = false;
				//Check the passenger status if it has initial status else you can't change to pickup status
				//e.g. if its dropped, then it can't be picked up again
				if (passenger.getStatus().equals(PassengerStatus.Confirmed)){
					passenger.setStatus(PassengerStatus.Picked);
					//Update the status in the db
					rideDO.update(ride);
				}else {
					throw new WebApplicationException("Passenger is not in valid state. Passenger current status:"+passenger.getStatus());
				}
			} if (passengerNotFound){
				throw new WebApplicationException("Passenger is not available for this ride. Passenger Id:"+passengerId);
			}
		} else {
			throw new WebApplicationException("Passenger can't be picked up as ride is not in valid state. Ride current statues:"+rideCurrentStatus);
		}
	}

	private RidePassenger getPassenger(Collection<RidePassenger> passengers, int passengerId){
		for (RidePassenger ridePassenger : passengers) {
			if (ridePassenger.getPassenger().getId() == passengerId){
				return ridePassenger;
			} 
		}	
		return null;
	}

	/*
	 * Purpose - Update the status of passenger to dropped state
	 * 
	 * High Level logic -
	 * 
	 * - Check if the ride is in started state
	 * - Check if passenger exist, This is actually not required, but doing it as extra check in the backend to make it full proof 
	 * - Check if the passenger is in pickup state
	 * - If yes, then update the passenger status to dropped state
	 * 
	 */
	public void dropPassenger(int rideId, int passengerId){
		Ride ride = rideDO.getChild(rideId);
		RideStatus rideCurrentStatus = ride.getStatus();
		//Check if ride is started
		if (rideCurrentStatus.equals(RideStatus.Started)){
			Collection<RidePassenger> passengers = ride.getPassengers();
			boolean passengerNotFound = true;
			//Get matching passenger from list of passengers
			RidePassenger passenger = getPassenger(passengers, passengerId);
			//This is actually not required, but doing it as extra check in the backend to make it full proof
			if (passenger!=null){
				passengerNotFound = false;
				//Check if passenger states is picked up
				if (passenger.getStatus().equals(PassengerStatus.Picked)){
					passenger.setStatus(PassengerStatus.Dropped);
					//Update the status in the db
					rideDO.update(ride);
				}else {
					throw new WebApplicationException("Passenger is not in valid state. Passenger current status:"+passenger.getStatus());					
				}
			} if (passengerNotFound){
				throw new WebApplicationException("Passenger is not available for this ride. Passenger Id:"+passengerId);
			}
		} else {
			throw new WebApplicationException("Passenger can't be dropped as ride is not in valid state. Ride current statues:"+rideCurrentStatus);
		}
	}

}









































