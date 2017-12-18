package com.digitusrevolution.rideshare.ride.domain.core.comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.exception.RideRequestUnavailableException;
import com.digitusrevolution.rideshare.common.exception.RideUnavailableException;
import com.digitusrevolution.rideshare.common.util.GoogleUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientImpl;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RidesInfo;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleGeocode;
import com.digitusrevolution.rideshare.model.ride.dto.google.Result;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
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
	public void acceptRideRequest(MatchedTripInfo matchedTripInfo){
		logger.debug("Accept Ride Request Request for [Ride Id/Ride Request Id]:"+matchedTripInfo.getRideId()+","+matchedTripInfo.getRideRequestId());
		int rideId = matchedTripInfo.getRideId();
		int rideRequestId = matchedTripInfo.getRideRequestId();
		Ride ride = rideDO.getAllData(rideId);
		RideStatus rideStatus = ride.getStatus();
		RideSeatStatus rideSeatStatus = ride.getSeatStatus();
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		RideRequestStatus rideRequestStatus = rideRequest.getStatus();

		int seatOccupied = 0;
		for (RideRequest acceptedRideRequest : ride.getAcceptedRideRequests()) {
			seatOccupied += acceptedRideRequest.getSeatRequired();
		}
		
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
				if (rideRequest.getSeatRequired() <= (ride.getSeatOffered() - seatOccupied)){

					//Set accepted ride in ride request
					//Note - By adding the ride request in the getAcceptedRideRequests collection, it will not update the ride id in the ride request table
					//as ride is acceptedRideRequests relationship is owned by ride request entity and not ride (@OneToMany(mappedBy="acceptedRide"))
					rideRequest.setAcceptedRide(ride);
					//Change ride request status to accept status
					rideRequest.setStatus(RideRequestStatus.Fulfilled);
					//Set Ride Pickup & Drop Points
					rideRequest.setRidePickupPoint(matchedTripInfo.getRidePickupPoint());
					rideRequest.setRideDropPoint(matchedTripInfo.getRideDropPoint());

					//This will get address from lat lng
					String rideRidePickupPointAddress = GoogleUtil.getAddress(rideRequest.getRidePickupPoint().getPoint().getLatitude(), 
							rideRequest.getRidePickupPoint().getPoint().getLongitude());
					String rideRideDropPointAddress = GoogleUtil.getAddress(rideRequest.getRideDropPoint().getPoint().getLatitude(), 
							rideRequest.getRideDropPoint().getPoint().getLongitude());

					//This will set Ride Pickup and Drop point address
					if (rideRidePickupPointAddress!=null) {
						rideRequest.setRidePickupPointAddress(rideRidePickupPointAddress);
					}
					if (rideRideDropPointAddress!=null) {
						rideRequest.setRideDropPointAddress(rideRideDropPointAddress);
					}

					rideRequest.setRidePickupPointDistance(matchedTripInfo.getPickupPointDistance());
					rideRequest.setRideDropPointDistance(matchedTripInfo.getDropPointDistance());

					//Adding passenger
					RidePassenger ridePassenger = new RidePassenger();
					ridePassenger.setPassenger(rideRequest.getPassenger());
					ridePassenger.setRide(ride);
					rideRequest.setPassengerStatus(PassengerStatus.Confirmed);
					ride.getRidePassengers().add(ridePassenger);

					//This will get the new status of seat post the acceptance of this ride request 
					//Seat status may or may not change depending on total seats occupied vs offered 
					RideSeatStatus newRideSeatStatus = getRideSeatStatusPostAcceptance(rideRequest.getSeatRequired(), ride);
					ride.setSeatStatus(newRideSeatStatus);
					
					//VERY Imp. - We are generating bill directly from here instead of going to Billing system so that we can do all this in one transaction
					//If we go to Billing system, then it has to be done post commit which will force to have another transaction 
					//and we need to do too many things to rollback manually
					float discountPercentage = 0;
					if (ride.getRideMode().equals(RideMode.Free)) discountPercentage = 100;
					Bill bill = generateBill(ride, rideRequest, discountPercentage);
					rideRequest.setBill(bill);

					//Update all the changes in DB for ride and ride request
					rideDO.update(ride);
					//This is required to update accepted ride as well as status update on ride request table
					rideRequestDO.update(rideRequest);
					//TODO Implement notification here
					logger.debug("Ride Request Accepted. Send Notification to Ride Owner & Requester on new Match. [Ride Id/Ride Request Id]:"+rideId+","+rideRequestId);
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
	 * Purpose - Generate bill for specific ride request
	 * 
	 * High level logic -
	 * 
	 * - Get Fare for the ride
	 * - Calculate total bill amount based on fare and travel distance
	 * - Calculate service charge based on service charge 
	 * - Set all the bill properties including status
	 * - Create bill in the system
	 * 
	 */
	public Bill generateBill(Ride ride, RideRequest rideRequest, float discountPercentage){
		
		User passenger = rideRequest.getPassenger();
		User driver = ride.getDriver();
		float price = getFare(ride.getVehicle().getVehicleSubCategory(), driver);
		float distance = rideRequest.getTravelDistance();
		float amount = price * distance;
		//This is post applying discount
		amount = amount * (100 - discountPercentage) / 100;
		Company company = RESTClientUtil.getCompany(1);
		float serviceChargePercentage = company.getServiceChargePercentage();
		Bill bill = new Bill();
		//Set Bill properties
		bill.setAmount(amount);
		bill.setDiscountPercentage(discountPercentage);
		bill.setServiceChargePercentage(serviceChargePercentage);
		bill.setCompany(company);
		bill.setDriver(driver);
		bill.setPassenger(passenger);
		bill.setRide(ride);
		bill.setRideRequest(rideRequest);
		bill.setStatus(BillStatus.Pending);
		return bill;
	}
	
	/*
	 * Purpose - Get fare rate per meter basis (Note - Its not per Km as all data in the system is in meters)
	 * 
	 */
	private float getFare(VehicleSubCategory vehicleSubCategory, User driver){
		FuelType fuelType = vehicleSubCategory.getFuelType();
		int averageMileage = vehicleSubCategory.getAverageMileage();
		Collection<Fuel> fuels = driver.getCountry().getFuels(); 
		for (Fuel fuel : fuels) {
			if (fuel.getType().name().equals(fuelType.name())){
				//This will get fare per meter and not by Km
				float fare = fuel.getPrice() / (averageMileage * 1000);
				return fare;
			}
		}
		throw new NotFoundException("Fuel type is not found. Fuel type is:"+fuelType);
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
	 * Purpose - Add ride request in the rejected list of ride. This function is only applicable if ride has not been accepted
	 * and User has choice of accepting or rejecting. So in that case this function would work but if ride has already been accepted
	 * be it by system or manually, then this is not the right method. Instead use cancelAcceptedRideRequest method
	 * 
	 * Note - Reason for returning void for consistency with other rides action method 
	 * as well as it will give flexibility to modify this function without worrying about updating both side references
	 * e.g ride should be updated with ride request and vice versa
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
	 * Note - Reason for returning void for consistency with other rides action method 
	 * as well as it will give flexibility to modify this function without worrying about updating both side references
	 * e.g ride should be updated with ride request and vice versa
	 * 
	 */
	public void startRide(int rideId){
		logger.debug("Start Ride:"+rideId);
		//Get child else child properties would get deleted while updating, as Ride Passenger has cascade enabled
		Ride ride = rideDO.getAllData(rideId);
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
	 * Note - Reason for returning void as we are not updating ride but ride request and there is no point returning old ride 
	 * as updated ride request would not come into effect until we commit the transaction
	 * 
	 */
	public void pickupPassenger(int rideId, int rideRequestId){
		logger.debug("Pickup Passenger for Ride Id/Ride RequestId:"+rideId+","+rideRequestId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		Ride ride = rideDO.getAllData(rideId);
		logger.debug("Passenger Count:"+ride.getRidePassengers().size());
		RideStatus rideCurrentStatus = ride.getStatus();
		//Check if ride is started
		if (rideCurrentStatus.equals(RideStatus.Started)){
			boolean passengerNotFound = true;
			//Get matching passenger from list of passengers
			RidePassenger passenger = ride.getRidePassenger(rideRequest.getPassenger().getId());
			//Check if passenger is found
			//As it may happen, that in between passenger has cancelled the ride request, so passenger would not be available 
			if (passenger!=null){
				passengerNotFound = false;
				//Check the passenger status if it has initial status else you can't change to pickup status
				//e.g. if its dropped, then it can't be picked up again
				if (rideRequest.getPassengerStatus().equals(PassengerStatus.Confirmed)){
					rideRequest.setPassengerStatus(PassengerStatus.Picked);
					//Update the status in the db
					rideRequestDO.update(rideRequest);					
				}else {
					throw new NotAcceptableException("Passenger is not in valid state. Passenger current status:"+rideRequest.getPassengerStatus());
				}
			} if (passengerNotFound){
				throw new NotAcceptableException("Passenger is not available for this ride. Passenger Id:"+rideRequest.getPassenger().getId());
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
	 * Note - Reason for returning void as we are not updating ride but ride request and there is no point returning old ride 
	 * as updated ride request would not come into effect until we commit the transaction
	 * 
	 */
	public void dropPassenger(int rideId, int rideRequestId){
		logger.debug("Drop Passenger for Ride Id/Ride RequestId:"+rideId+","+rideRequestId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		Ride ride = rideDO.getAllData(rideId);
		RideStatus rideCurrentStatus = ride.getStatus();
		//Check if ride is started
		if (rideCurrentStatus.equals(RideStatus.Started)){
			boolean passengerNotFound = true;
			//Get matching passenger from list of passengers
			RidePassenger ridePassenger = ride.getRidePassenger(rideRequest.getPassenger().getId());
			//This is actually not required, but doing it as extra check in the backend to make it full proof
			if (ridePassenger!=null){
				passengerNotFound = false;
				//Check if passenger states is picked up
				if (rideRequest.getPassengerStatus().equals(PassengerStatus.Picked)){
					rideRequest.setPassengerStatus(PassengerStatus.Dropped);
					//Update the status in the db
					rideRequestDO.update(rideRequest);
				}else {
					throw new NotAcceptableException("Passenger is not in valid state. Passenger current status:"+rideRequest.getPassengerStatus());					
				}
			} if (passengerNotFound){
				throw new NotAcceptableException("Passenger is not available for this ride. Passenger Id:"+rideRequest.getPassenger().getId());
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
	 * - Reason for dropping / canceling by system instead of asking driver to do that as its mandatory to drop/cancel all the passengers
	 *   before ending the ride and it will not create any difference if driver/system do this task, so for convenience system will take care of it 
	 * - Update the ride status to finished
	 * 
	 * Note - Reason for returning void as we are not just updating ride but also ride request inside drop and pickup function and there is no point returning old ride 
	 * as updated ride request would not come into effect until we commit the transaction
	 * 
	 */
	public void endRide(int rideId){
		logger.debug("Ending Ride:"+rideId);
		Ride ride = rideDO.getAllData(rideId);
		RideStatus rideStatus = ride.getStatus();
		//Check if the ride has been started
		if (rideStatus.equals(RideStatus.Started)){
			boolean passengerOnBoard = false;
			boolean passengerNotPicked = false;
			List<RideRequest> onBoardedPassengerRideRequestList = new ArrayList<>();
			List<RideRequest> notPickedPassengerRideRequestList = new ArrayList<>();
			Collection<RideRequest> rideRequests = ride.getAcceptedRideRequests();
			for (RideRequest rideRequest:rideRequests) {
				if (rideRequest.getPassengerStatus().equals(PassengerStatus.Dropped)){
					continue;
				}
				if (rideRequest.getPassengerStatus().equals(PassengerStatus.Picked)){
					passengerOnBoard = true;
					onBoardedPassengerRideRequestList.add(rideRequest);
					continue;
				}
				if (rideRequest.getPassengerStatus().equals(PassengerStatus.Confirmed)){
					passengerNotPicked = true;
					notPickedPassengerRideRequestList.add(rideRequest);
				}
			}

			//This is the scenario, when some/all passenger has not been picked
			if (passengerNotPicked){
				for (RideRequest rideRequest : notPickedPassengerRideRequestList) {
					//Cancel all the ride request which has not been picked
					//We can also ask driver to cancel all ride request, but for convenience system would take care of it 
					//as it doesn't change anything from driver end 
					//Note - Reason for passing the cancelRideRequest as false, as we don't want ride request to be cancelled as well
					cancelAcceptedRideRequest(rideId, rideRequest.getId(), false);
				}
			}
			//This is the scenario when all has been picked but some/all not dropped
			if (passengerOnBoard){
				//Drop all the onboarded passenger who has not been dropped
				//We can also ask driver to drop all passenger, but for convinience system would take care of it 
				//as it doesn't change anything from driver end 
				for (RideRequest rideRequest : onBoardedPassengerRideRequestList) {
					dropPassenger(rideId, rideRequest.getId());
				}
			}

			//So now all passenger has been either dropped / cancelled
			//Change the ride status to finished
			//Note - You can't update the status upfront, otherwise drop/cancellation would not work if ride is in Finished state
			//That's why you need to get the updated ride before updating the status, else it will overwrite the status which may have been 
			//changed while dropping the passenger or cancelling the rid request
			ride = rideDO.getAllData(rideId);
			ride.setStatus(RideStatus.Finished);
			rideDO.update(ride);
		} else {
			throw new NotAcceptableException("Ride can't be ended as its not in valid state. Current ride status:"+rideStatus);
		}
	}

	/*
	 * Purpose - Cancel ride request post acceptance
	 * 
	 * Note - This cancellation is different than passenger cancelling their ride request on their own
	 * 
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
	 * - If cancelRideRequest status is true, then change the status to cancelled (This is just to support all action in one transaction 
	 *   and avoid issue of returning updated ride and ride request to another function which may be a challenge within the same transaction before commit)
	 * 
	 * Note - Reason for returning void for consistency with other rides action method 
	 * as well as it will give flexibility to modify this function without worrying about updating both side references
	 * e.g ride should be updated with ride request and vice versa
	 * 
	 */
	public void cancelAcceptedRideRequest(int rideId, int rideRequestId, boolean cancelRideRequest){
		logger.debug("Cancelling Accepted Ride Request - ride Id/Ride Request Id:"+rideId+","+rideRequestId);
		RidesInfo ridesInfo = new RidesInfo();
		Ride ride = rideDO.getAllData(rideId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		RideStatus rideStatus = ride.getStatus();
		RidePassenger ridePassenger = ride.getRidePassenger(rideRequest.getPassenger().getId());
		PassengerStatus passengerStatus = rideRequest.getPassengerStatus();
		logger.debug("Passenger Count pre cancellation:"+ride.getAcceptedRideRequests().size());
		//Check if ride request has been accepted by this ride
		//Accepted ride would be null for unfulfilled ride request, so need to check for null condition 
		if (rideRequest.getAcceptedRide() !=null ? rideRequest.getAcceptedRide().getId() == ride.getId() : false){
			//Check if ride has not been cancelled or finished
			if (!rideStatus.equals(RideStatus.Cancelled) || !rideStatus.equals(RideStatus.Finished)){
				//Check if passenger is in confirmed state
				if (passengerStatus.equals(PassengerStatus.Confirmed)){
					//IMP - Remove passenger from the list, else it would add again due to cascade effect
					//Reason for deleting using RidePassengerDO and not just by removing from the ride list
					//as deletion is not working and it may be because we are not updating ride passenger of passenger 
					//And since, Ride passenger has OneToMany relationship from Ride as well as User side, so updating from one side 
					//may not be sufficient. We are not able to update User side as we are not maintaining ride passenger at User domain model
					ride.getRidePassengers().remove(ridePassenger);
					RidePassengerDO ridePassengerDO = new RidePassengerDO();
					ridePassengerDO.delete(ridePassenger.getId());
					//Ride seat status would always become available as we are freeing up at least one seat in any case
					//So even if its Unavailable before cancellation, this will make seat available
					ride.setSeatStatus(RideSeatStatus.Available);
					//Change ride request status to Unfulfilled
					rideRequest.setStatus(RideRequestStatus.Unfulfilled);
					//Note - This is only applicable if we are planning to return update ride and ride request from this function
					//but for the time being we have changed the return type to void and we will think if we need to return any value at all later
					//so for now lets have below statements as it is as there is no harm
					
					//VERY IMP - This will ensure ride request is also removed from the ride, so that we are able to get updated ride post updation of ride and ride request
					//If you don't do this then you will get ride with same accepted ride requests which you wanted to remove
					//So in nutsehll, within a transaction if you want to return the data then ensure all sides data is updated 
					//e.g ride has ride request and ride request has ride, so update both side property from ride as well as ride request for accepted ride 
					ride.getAcceptedRideRequests().remove(rideRequest);
					//This is important which will make ride request back to the original state by resetting all fields related to this matched Ride
					resetRideRequestFieldsRelatedToMatchedRide(rideRequest);
					//Add ride request in cancelled list
					ride.getCancelledRideRequests().add(rideRequest);
					//Reason for doing this so that we get the updated cancelled ride within this transaction itself
					rideRequest.getCancelledRides().add(ride);
					
					//Note - This will take care of cancelling ride request as well in this function itself, 
					//otherwise we are having issue in returning updated ride request before committing the transaction
					//and since we are trying to do cancellation of ride request in Ride RequestDO this may become challenge
					//so cleaner approach is to do all in one go
					
					if (cancelRideRequest) {
						rideRequest.setStatus(RideRequestStatus.Cancelled);
					}
					
					//This will update ride and ride request in db
					rideRequestDO.update(rideRequest);
					rideDO.update(ride); 
					logger.debug("Passenger Count post cancellation:"+ride.getAcceptedRideRequests().size());
					ridesInfo.setRide(ride);
					ridesInfo.setRideRequest(rideRequest);
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

	private void resetRideRequestFieldsRelatedToMatchedRide(RideRequest rideRequest) {
		//Remove accepted ride from ride request
		rideRequest.setAcceptedRide(null);
		//Reset Ride Pickup and Drop related fields from ride request
		//Its important to set Id as null and not the Point as we are storing only ID in SQL DB else you will get NPE
		rideRequest.getRideDropPoint().set_id(null);
		rideRequest.setRideDropPointAddress(null);
		rideRequest.setRidePickupPointDistance(0);
		//Its important to set Id as null and not the Point as we are storing only ID in SQL DB else you will get NPE
		rideRequest.getRidePickupPoint().set_id(null);
		rideRequest.setRidePickupPointAddress(null);
		rideRequest.setRideDropPointDistance(0);
		rideRequest.setPassengerStatus(PassengerStatus.Unconfirmed);
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
	 * Note - Reason for returning void for consistency with other rides action method 
	 * as well as it will give flexibility to modify this function without worrying about updating both side references
	 * e.g ride should be updated with ride request and vice versa
	 * 
	 */
	public void cancelRide(int rideId){
		logger.debug("Cancelling Ride:"+rideId);
		Ride ride = rideDO.getAllData(rideId);
		RideStatus rideStatus = ride.getStatus();
		//Check if ride has not been finished
		if (!rideStatus.equals(RideStatus.Finished)){
			Collection<RideRequest> acceptedRideRequests = ride.getAcceptedRideRequests();
			for (RideRequest rideRequest : acceptedRideRequests) {
				//Note - Reason for passing the cancelRideRequest as false, as we don't want ride request to be cancelled as well
				cancelAcceptedRideRequest(rideId, rideRequest.getId(), false);
				//This will take care of re-matching effected ride requests
				//IMP - I am not doing this inside canceAcceptedRideRequest as that function is called by even cancelRideRequest
				//and if i do auto match there then for cancel ride request case, first it will cancel the ride request and then do 
				//auto match for the same ride request which would be wrong.
				rideDO.autoMatchRide(rideRequest.getId());
			}
			//Update the ride status as cancelled and update the db
			ride.setStatus(RideStatus.Cancelled);
			rideDO.update(ride);
		} else {
			throw new NotAcceptableException("Ride can't be cancelled as its not in valid state. Current ride status:"+rideStatus);
		}
	}
}









































