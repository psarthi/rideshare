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

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.exception.RideRequestUnavailableException;
import com.digitusrevolution.rideshare.common.exception.RideUnavailableException;
import com.digitusrevolution.rideshare.common.service.NotificationService;
import com.digitusrevolution.rideshare.common.util.GoogleUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientImpl;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.common.NotificationMessage;
import com.digitusrevolution.rideshare.model.ride.domain.CancellationType;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RidesInfo;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleGeocode;
import com.digitusrevolution.rideshare.model.ride.dto.google.Result;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.UserFeedbackInfo;
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
	 * Note - We are passing ride and rideRequest instead of getting the same within this function, so that we can maintain the sanity of the data
	 * and perform multiple operation within the same transaction. Otherwise what happens until we commit transaction we don't get updated data
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
	public void acceptRideRequest(Ride ride, RideRequest rideRequest, MatchedTripInfo matchedTripInfo){
		logger.info("Accept Ride Request Request for [Ride Id/Ride Request Id]:"+matchedTripInfo.getRideId()+","+matchedTripInfo.getRideRequestId());
		long rideId = matchedTripInfo.getRideId();
		long rideRequestId = matchedTripInfo.getRideRequestId();
		//Ride ride = rideDO.getAllData(rideId);
		RideStatus rideStatus = ride.getStatus();
		RideSeatStatus rideSeatStatus = ride.getSeatStatus();
		RideRequestDO rideRequestDO = new RideRequestDO();
		//RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
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
					//This will just help us to maintain the sanity of data within the transaction so that we can perform more actions within this transaction itself
					//But yes, as mentioned above this will not add ride to ride request for that you need to setAcceptedRide only 
					ride.getAcceptedRideRequests().add(rideRequest);

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
					RideSeatStatus newRideSeatStatus = getRideSeatStatusPostAcceptance(rideRequest.getSeatRequired(), seatOccupied, ride.getSeatOffered());
					ride.setSeatStatus(newRideSeatStatus);

					Bill bill = generateBill(ride, rideRequest);

					if (rideRequest.getBill()!=null) {
						//IMP - This will update the bill instead of regeneration
						//Instead of deletion and maintaining the transaction rollback issue, its easy to update
						//this may have some implication of data cleanliness but we need to handle programmatically till we get cleaner solution
						bill.setNumber(rideRequest.getBill().getNumber());
					} 
					rideRequest.setBill(bill);

					boolean sufficientBalance = false; 
					//Payment Verification code is only applicable if ride is paid mode
					//Check balance also application for only ride paid mode
					if (ride.getRideMode().equals(RideMode.Paid)) {
						//This will act as payment confirmation code for debiting money from passenger account
						rideRequest.setConfirmationCode(AuthService.getInstance().getVerificationCode());
						sufficientBalance = checkSufficientBalance(rideRequest); 
					}
					else {
						//This will ensure that balance doesn't matter for Free matched ride
						sufficientBalance = true;
					}

					if (sufficientBalance) {
						//Update all the changes in DB for ride and ride request
						rideDO.update(ride);
						//This is required to update accepted ride as well as status update on ride request table
						rideRequestDO.update(rideRequest);
						//TODO Implement notification here
						NotificationService.sendMatchedRideNotification(ride, rideRequest);
						NotificationService.sendRideMatchAdminNotification(ride, rideRequest);
						logger.info("Ride Request Accepted.[Ride Id/Ride Request Id]:"+rideId+","+rideRequestId);						
					} else {
						NotificationService.sendInsufficientBalanceNotification(rideRequest);
						logger.info("Ride Request can't be accepted due to insufficient balance.[Ride Id/Ride Request Id]:"+rideId+","+rideRequestId);
						throw new InSufficientBalanceException("Not enough balance to pay for the bill of ride request Id:"+rideRequestId);
					}
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
	 * Purpose - This will check if the balance is sufficient enough to pay for this ride request
	 */
	private boolean checkSufficientBalance(RideRequest rideRequest) {
		BasicUser passenger = JsonObjectMapper.getMapper().convertValue(rideRequest.getPassenger(), BasicUser.class);
		List<Bill> pendingBills = RESTClientUtil.getPendingBills(passenger);
		float pendingAmount = 0;
		for (Bill bill: pendingBills) {
			pendingAmount+=bill.getAmount();
		}

		ArrayList<Account> accounts = (ArrayList<Account>) passenger.getAccounts();
		float balance = accounts.get(0).getBalance();

		float requiredBalance = pendingAmount + rideRequest.getBill().getAmount();
		if (balance >= requiredBalance) {
			return true;
		} 
		return false;
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
	public Bill generateBill(Ride ride, RideRequest rideRequest){

		User passenger = rideRequest.getPassenger();
		User driver = ride.getDriver();
		float discountPercentage;
		float amount;
		//IMP - This will take care of scenario when ride request is Paid but ride is Free 
		if (ride.getRideMode().equals(RideMode.Free)) {
			amount = 0;
			discountPercentage = 100;
		} else {
			amount = getPrice(rideRequest);
			discountPercentage = getDiscountPercentage(rideRequest);
		}
		Company company = RESTClientUtil.getCompany(1);
		float serviceChargePercentage = company.getServiceChargePercentage();
		//Note - Fare and Discount is used here just to store in the bill but calculation
		//of the discount is already taken care as part of price calculation
		float farePerMeter = getFarePerMeter(rideRequest.getVehicleSubCategory(), rideRequest.getPassenger());
		float farePerKm = farePerMeter * 1000;
		Bill bill = new Bill();
		//Set Bill properties
		bill.setAmount(amount);
		bill.setRate(farePerKm);
		bill.setDiscountPercentage(discountPercentage);
		bill.setServiceChargePercentage(serviceChargePercentage);
		bill.setCompany(company);
		bill.setDriver(driver);
		bill.setPassenger(passenger);
		bill.setRide(ride);
		bill.setRideRequest(rideRequest);
		if (discountPercentage==100) {
			bill.setStatus(BillStatus.Free);
		} else {
			bill.setStatus(BillStatus.Pending);	
		}
		return bill;
	}

	public float getPrice(RideRequest rideRequest) {
		float farePerMeter = getFarePerMeter(rideRequest.getVehicleSubCategory(), rideRequest.getPassenger());
		//Distance is in meters
		float distance = rideRequest.getTravelDistance();
		float amount = farePerMeter * distance * rideRequest.getSeatRequired();
		float discountPercentage = getDiscountPercentage(rideRequest);
		//This is post applying discount
		amount = amount * (100 - discountPercentage) / 100;
		return amount;
	}

	private float getDiscountPercentage(RideRequest rideRequest) {
		//VERY Imp. - We are generating bill directly from here instead of going to Billing system so that we can do all this in one transaction
		//If we go to Billing system, then it has to be done post commit which will force to have another transaction 
		//and we need to do too many things to rollback manually
		float discountPercentage = 0;
		if (rideRequest.getRideMode().equals(RideMode.Free)) {
			discountPercentage = 100;
		}
		//This is for paid mode and for long distance, where fuel cost is shared
		//IMP - If we don't share the fuel cost, then travel cost would be much higher than public transports
		else {
			int longDistance = Integer.parseInt(PropertyReader.getInstance().getProperty("LONG_DISTANCE_IN_METERS"));
			if (rideRequest.getTravelDistance() >= longDistance) {
				discountPercentage = 50;
			}
		}
		return discountPercentage;
	}

	/*
	 * Purpose - Get fare rate per meter basis (Note - Its not per Km as all data in the system is in meters)
	 * 
	 */
	private float getFarePerMeter(VehicleSubCategory vehicleSubCategory, User driver){
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
	private RideSeatStatus getRideSeatStatusPostAcceptance(int requiredSeats, int seatOccupied, int seatOffered){
		//This should include the current required seat as we need to calculate total seats including this ride request 
		int seatsOccupiedPostAcceptance = requiredSeats + seatOccupied;

		//This will check if seats offered is equal to the accepted ride request including this ride request
		if (seatsOccupiedPostAcceptance == seatOffered){
			return RideSeatStatus.Unavailable;
		} else {
			return RideSeatStatus.Available;
		}	
	}	

	/*
	 * Purpose - Change the ride status to started status
	 * 
	 * High Level Logic -
	 * 
	 * - Check the status of ride to see if its in planned state or fulfilled state
	 * - If its in valid state, then change the status to started
	 * 
	 * Note - Reason for returning void for consistency with other rides action method 
	 * as well as it will give flexibility to modify this function without worrying about updating both side references
	 * e.g ride should be updated with ride request and vice versa
	 * 
	 */
	public void startRide(long rideId){
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
	public void pickupPassenger(long rideId, long rideRequestId){
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
	public void dropPassenger(long rideId, long rideRequestId, RideMode rideMode, String paymentCode){
		logger.info("Drop Passenger for Ride Id/Ride RequestId:"+rideId+","+rideRequestId);
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

					//If its a Free Ride originally, then no need to do any payment or change the bill status
					if (!rideRequest.getBill().getStatus().equals(BillStatus.Free)) {
						//This will take care in case of change of ride mode at the time of dropping
						if (rideMode.equals(RideMode.Free)) {
							rideRequest.getBill().setDiscountPercentage(100);
							rideRequest.getBill().setAmount(0);
							//Payment not required here as its a free ride, so calls to billing system to makePayment, 
							//only we need to updated the BillStatus without any transaction
							rideRequest.getBill().setStatus(BillStatus.Free);
							logger.info("Its a Free Ride, so no payment is required for Ride Request Id:"+rideRequestId);
						} 
						//This is scenario for Paid ride
						else {
							if(rideRequest.getConfirmationCode().equals(paymentCode)){
								//Not doing payment here to avoid any transactional failures in between, 
								//instead we will do payment post successful drop in separate transaction
								rideRequest.getBill().setStatus(BillStatus.Approved);
								logger.info("Its a Paid Ride, and payment is approved for Ride Request Id:"+rideRequestId);
							} else {
								throw new NotAcceptableException("Payment code is invalid for the ride request"+rideRequest.getId());
							}
						}
					}

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
	public void endRide(long rideId){
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
					cancelAcceptedRideRequest(rideId, rideRequest.getId(), CancellationType.Ride);
				}
			}

			//This is the scenario when some/all passenger has not been dropped
			if (passengerOnBoard){
				//Drop all the onboarded passenger who has not been dropped
				//We can also ask driver to drop all passenger, but for convinience system would take care of it 
				//as it doesn't change anything from driver end
				//Note - Lets not allow auto drop of the passenger as this would affect payment confirmation flow
				/*
				for (RideRequest rideRequest : onBoardedPassengerRideRequestList) {
					dropPassenger(rideId, rideRequest.getId());
				}*/
				throw new NotAcceptableException("You have passenger on-board, please drop them first");
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
	 * - Check if ride passenger has not been dropped (Allow user to cancel even he/she has been picked to avoid unnecessary blocking of their money and misuse from driver side)
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
	public void cancelAcceptedRideRequest(long rideId, long rideRequestId, CancellationType cancellationType){
		logger.info("Cancelling Accepted Ride Request - ride Id/Ride Request Id:"+rideId+","+rideRequestId);
		//RidesInfo ridesInfo = new RidesInfo();
		Ride ride = rideDO.getAllData(rideId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		RideStatus rideStatus = ride.getStatus();
		RidePassenger ridePassenger = ride.getRidePassenger(rideRequest.getPassenger().getId());
		PassengerStatus passengerStatus = rideRequest.getPassengerStatus();
		logger.info("Passenger Count pre cancellation:"+ride.getAcceptedRideRequests().size());
		//Check if ride request has been accepted by this ride
		//Accepted ride would be null for unfulfilled ride request, so need to check for null condition 
		if (rideRequest.getAcceptedRide() !=null ? rideRequest.getAcceptedRide().getId() == ride.getId() : false){
			//Check if ride has not been cancelled or finished
			if (!rideStatus.equals(RideStatus.Cancelled) || !rideStatus.equals(RideStatus.Finished)){
				//Check if passenger has not been dropped
				if (!passengerStatus.equals(PassengerStatus.Dropped)){
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

					//This will change the status of Bill to Cancelled
					rideRequest.getBill().setStatus(BillStatus.Cancelled);

					//Note - This will take care of cancelling ride request as well in this function itself, 
					//otherwise we are having issue in returning updated ride request before committing the transaction
					//and since we are trying to do cancellation of ride request in Ride RequestDO this may become challenge
					//so cleaner approach is to do all in one go

					if (cancellationType.equals(CancellationType.RideRequest)) {
						rideRequest.setStatus(RideRequestStatus.Cancelled);
					}

					//This will update ride and ride request in db
					rideRequestDO.update(rideRequest);
					rideDO.update(ride); 
					NotificationService.sendCancelRideNotification(ride, rideRequest, cancellationType);
					logger.info("RideRequest has been cancelled for Id:"+rideRequest.getId());
					logger.info("Passenger Count post cancellation:"+ride.getAcceptedRideRequests().size());
					//Commenting this as its not in use by anyone
					//ridesInfo.setRide(ride);
					//ridesInfo.setRideRequest(rideRequest);
				} else {
					throw new NotAcceptableException("Ride request can't be cancelled as Passenger has already been dropped."
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
	public void cancelRide(long rideId){
		logger.info("Cancelling Ride:"+rideId);
		Ride ride = rideDO.getAllData(rideId);
		RideStatus rideStatus = ride.getStatus();
		//Check if ride has not been finished
		if (!rideStatus.equals(RideStatus.Finished)){
			Collection<RideRequest> acceptedRideRequests = ride.getAcceptedRideRequests();
			for (RideRequest rideRequest : acceptedRideRequests) {
				//Note - Reason for passing the cancelRideRequest as false, as we don't want ride request to be cancelled as well
				cancelAcceptedRideRequest(rideId, rideRequest.getId(), CancellationType.Ride);
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









































