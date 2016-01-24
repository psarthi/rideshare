package com.digitusrevolution.rideshare.ride.domain.core.comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.NotAcceptableException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.exception.RideRequestUnavailableException;
import com.digitusrevolution.rideshare.common.exception.RideUnavailableException;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RidePassengerDO;
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
	 * - Check status of ride request to see if its still looking for ride i.e. its unfulfilled
	 * - Check status of ride to see if seats are still available
	 * - Check ride has not been cancelled or finished
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
		Ride ride = rideDO.getChild(rideId);
		RideStatus rideStatus = ride.getStatus();
		RideSeatStatus rideSeatStatus = ride.getSeatStatus();
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.get(rideRequestId);
		RideRequestStatus rideRequestStatus = rideRequest.getStatus();


		//Check if ride request is unfulfilled
		//Reason for re-checking status criteria as from the time of search to responding to it, 
		//there may be someone else who may have accepted the ride request already and status may have changed
		if (rideRequestStatus.equals(RideRequestStatus.Unfulfilled)){
			//This will check if ride seats are available, ride is not cancelled and neither finished
			//Reason for re-checking seat status criteria as ride may have accepted some other ride request and seats may have got full, when trying to accept
			//another one ride may not be available
			if (!rideStatus.equals(RideStatus.Cancelled) && !rideStatus.equals(RideStatus.Finished) 
					&& rideSeatStatus.equals(RideSeatStatus.Available)){
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
					ridePassenger.setRide(ride);
					ridePassenger.setStatus(PassengerStatus.Confirmed);
					ride.getRidePassengers().add(ridePassenger);

					//This will get the new status of seat post the acceptance of this ride request 
					//Seat status may or may not change depending on total seats occupied vs offered 
					RideSeatStatus newRideSeatStatus = getRideSeatStatusPostAcceptance(rideRequest.getSeatRequired(), ride);
					ride.setSeatStatus(newRideSeatStatus);

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
	 * Purpose - This will check the ride seat status post the acceptance of new ride request
	 * 
	 */
	private RideSeatStatus getRideSeatStatusPostAcceptance(int requiredSeats, Ride ride){
		//This should include the current required seat as we need to calculate total seats including this ride request 
		int totalSeatsOccupied = requiredSeats;
		//Since each ride request may have different seat requirement, so we need to calculate total of all required seats of accepted ride
		//Note - Seats requirement and seat offered criteria should be met while searching for ride or ride requests
		for (RideRequest acceptedRideRequest : ride.getAcceptedRideRequests()) {
			totalSeatsOccupied = totalSeatsOccupied + acceptedRideRequest.getSeatRequired();
		}

		//This will check if seats offered is equal to the accepted ride request including this ride request
		if (totalSeatsOccupied == ride.getSeatOffered()){
			return RideSeatStatus.Unavailable;
		} else {
			return RideSeatStatus.Available;
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
	 * - Check the status of ride to see if its in planned state 
	 * - If its in valid state, then change the status to started
	 * 
	 */
	public void startRide(int rideId){
		//Get child else child properties would get deleted while updating, as Ride Passenger has cascade enabled
		Ride ride = rideDO.getChild(rideId);
		RideStatus rideCurrentStatus = ride.getStatus();
		if (rideCurrentStatus.equals(RideStatus.Planned)){
			ride.setStatus(RideStatus.Started);
			rideDO.update(ride);			
		} else {
			throw new NotAcceptableException("Ride can't be started as its not in valid state. Ride current status:"+rideCurrentStatus);
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
		logger.debug("Passenger Count:"+ride.getRidePassengers().size());
		RideStatus rideCurrentStatus = ride.getStatus();
		//Check if ride is started
		if (rideCurrentStatus.equals(RideStatus.Started)){
			boolean passengerNotFound = true;
			//Get matching passenger from list of passengers
			RidePassenger passenger = ride.getRidePassenger(passengerId);
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
					throw new NotAcceptableException("Passenger is not in valid state. Passenger current status:"+passenger.getStatus());
				}
			} if (passengerNotFound){
				throw new NotAcceptableException("Passenger is not available for this ride. Passenger Id:"+passengerId);
			}
		} else {
			throw new NotAcceptableException("Passenger can't be picked up as ride is not in valid state. Ride current statues:"+rideCurrentStatus);
		}
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
			boolean passengerNotFound = true;
			//Get matching passenger from list of passengers
			RidePassenger ridePassenger = ride.getRidePassenger(passengerId);
			//This is actually not required, but doing it as extra check in the backend to make it full proof
			if (ridePassenger!=null){
				passengerNotFound = false;
				//Check if passenger states is picked up
				if (ridePassenger.getStatus().equals(PassengerStatus.Picked)){
					ridePassenger.setStatus(PassengerStatus.Dropped);
					//Update the status in the db
					rideDO.update(ride);
				}else {
					throw new NotAcceptableException("Passenger is not in valid state. Passenger current status:"+ridePassenger.getStatus());					
				}
			} if (passengerNotFound){
				throw new NotAcceptableException("Passenger is not available for this ride. Passenger Id:"+passengerId);
			}
		} else {
			throw new NotAcceptableException("Passenger can't be dropped as ride is not in valid state. Ride current statues:"+rideCurrentStatus);
		}
	}

	/*
	 * Purpose - End the ride
	 * 
	 * Key Note - 
	 * 
	 * There may be a situation where some of the passenger was not dropped by driver and system has dropped them, 
	 * So when you call endRide, post that check the status of all bills and if any of the bill is not generated for any passenger
	 * then generate the bill and follow the process of requesting payment
	 *        
	 * High level logic -
	 * 
	 * - Check if the ride has been started
	 * - Check if there is any passenger who has not been picked or dropped
	 * - Drop all the passenger who is on board
	 * - Cancel all the ride request who has not been picked up
	 * - Reason for dropping / cancelling by system instead of asking driver to do that as its mandatory to drop/cancel all the passengers
	 *   before ending the ride and it will not create any difference if driver/system do this task, so for convenience system will take care of it 
	 * - Update the ride status to finished
	 * 
	 */
	public void endRide(int rideId){
		Ride ride = rideDO.getChild(rideId);
		RideStatus rideStatus = ride.getStatus();
		//Check if the ride has been started
		if (rideStatus.equals(RideStatus.Started)){
			boolean passengerOnBoard = false;
			boolean passengerNotPicked = false;
			List<User> onBoardedPassengerList = new ArrayList<>();
			List<User> notPickedPassengerList = new ArrayList<>();
			Collection<RidePassenger> ridePassengers = ride.getRidePassengers();
			for (RidePassenger ridePassenger : ridePassengers) {
				if (ridePassenger.getStatus().equals(PassengerStatus.Dropped)){
					continue;
				}
				if (ridePassenger.getStatus().equals(PassengerStatus.Picked)){
					passengerOnBoard = true;
					onBoardedPassengerList.add(ridePassenger.getPassenger());
					continue;
				}
				if (ridePassenger.getStatus().equals(PassengerStatus.Confirmed)){
					passengerNotPicked = true;
					notPickedPassengerList.add(ridePassenger.getPassenger());
				}
			}

			//This is the scenario, when some/all passenger has not been picked
			if (passengerNotPicked){
				for (User passenger : notPickedPassengerList) {
					RideRequest rideRequest = ride.getRideRequestOfPassenger(passenger.getId());
					//Cancel all the ride request which has not been picked
					//We can also ask driver to cancel all ride request, but for convinience system would take care of it 
					//as it doesn't change anything from driver end 
					cancelRideRequest(rideId, rideRequest.getId());
				}
			}
			//This is the scenario when all has been picked but some/all not dropped
			if (passengerOnBoard){
				//Drop all the onboarded passenger who has not been dropped
				//We can also ask driver to drop all passenger, but for convinience system would take care of it 
				//as it doesn't change anything from driver end 
				for (User passenger : onBoardedPassengerList) {
					dropPassenger(rideId, passenger.getId());
				}
			}
			//Now all passenger has been either dropped or cancelled, so no passenger on board
			//Change the ride status to finished
			ride.setStatus(RideStatus.Finished);
			rideDO.update(ride);
		}
	}

	/*
	 * Purpose - Cancel ride request post acceptance
	 * 
	 * High level logic -
	 * 
	 * - Check if ride request has been accepted
	 * - Check if ride passenger state is in confirmed
	 * - If yes, then add ride request in cancelled ride requests collection
	 * - Remove passenger of that ride request from ride passenger collection
	 * - Change the ride seat status to available - This would always become available as we are freeing up at least one seat in any case,
	 *   so even if its Unavailable before cancellation, this will make seat available
	 * - Change Ride request status to Unfulfilled
	 * - Remove accepted ride from ride requests 
	 * 
	 */
	public void cancelRideRequest(int rideId, int rideRequestId){
		Ride ride = rideDO.getChild(rideId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getChild(rideRequestId);
		RideStatus rideStatus = ride.getStatus();
		RidePassenger ridePassenger = ride.getRidePassenger(rideRequest.getPassenger().getId());
		PassengerStatus passengerStatus = ridePassenger.getStatus();
		//Check if ride request has been accepted by this ride
		//Accepted ride would be null for unfulfilled ride request, so need to check for null condition 
		if (rideRequest.getAcceptedRide() !=null ? rideRequest.getAcceptedRide().getId() == ride.getId() : false){
			//Check if ride has not been cancelled or finished
			if (!rideStatus.equals(RideStatus.Cancelled) || !rideStatus.equals(RideStatus.Finished)){
				//Check if passenger is in confirmed state
				if (passengerStatus.equals(PassengerStatus.Confirmed)){
					//IMP - Remove passenger from the list, else it would add again due to cascade effect
					ride.getRidePassengers().remove(ridePassenger);
					RidePassengerDO ridePassengerDO = new RidePassengerDO();
					ridePassengerDO.delete(ridePassenger.getId());
					//Ride seat status would always become available as we are freeing up at least one seat in any case
					//So even if its Unavailable before cancellation, this will make seat available
					ride.setSeatStatus(RideSeatStatus.Available);
					//Change ride request status to Unfulfilled
					rideRequest.setStatus(RideRequestStatus.Unfulfilled);
					//Remove accepted ride from ride request
					rideRequest.setAcceptedRide(null);
					//Add ride request in cancelled list
					ride.getCancelledRideRequests().add(rideRequest);
					//This will update ride and ride request in db
					rideDO.update(ride);
					rideRequestDO.update(rideRequest);
				} else {
					throw new NotAcceptableException("Ride request can't be cancelled as Passenger has already been picked up. "
							+ "Passenger current status:"+passengerStatus);
				}
			} else {
				throw new NotAcceptableException("Ride request can't be cancelled as ride is not in valid state. Ride current status:"+rideStatus);
			}
		} else {
			throw new NotAcceptableException("Ride request can't be cancelled as currently this ride is not mapped to the ride request."
					+ "Ride id:"+rideId+",Ride Request id:"+rideRequestId);
		}
	}
	
	/*
	 * Purpose - Cancel ride
	 * 
	 * High level logic -
	 * 
	 * - Check if ride has been finished, if yes, you can't cancel the ride
	 * - Else, do the following
	 * - Cancel all the accepted ride request
	 * - Update the ride status as cancelled
	 * 
	 */
	public void cancelRide(int rideId){
		
		Ride ride = rideDO.getChild(rideId);
		RideStatus rideStatus = ride.getStatus();
		//Check if ride has not been finished
		if (!rideStatus.equals(RideStatus.Finished)){
			Collection<RideRequest> acceptedRideRequests = ride.getAcceptedRideRequests();
			for (RideRequest rideRequest : acceptedRideRequests) {
				cancelRideRequest(rideId, rideRequest.getId());
			}
			//Update the ride status as cancelled and update the db
			ride.setStatus(RideStatus.Cancelled);
			rideDO.update(ride);
		}
	}

}









































