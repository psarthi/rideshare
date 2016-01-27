package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.billing.data.BillDAO;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.domain.core.BillStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;
import com.digitusrevolution.rideshare.model.user.domain.FuelType;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class BillDO implements DomainObjectPKInteger<Bill>{
	
	private Bill bill;
	private final BillDAO billDAO = new BillDAO();
	private static final Logger logger = LogManager.getLogger(BillDO.class.getName());
	
	@Override
	public List<Bill> getAll() {
		List<Bill> bills = new ArrayList<>();
		List<BillEntity> billEntities = billDAO.getAll();
		for (BillEntity billEntity : billEntities) {
			Bill bill = new Bill();
			bill.setEntity(billEntity);
			bills.add(bill);
		}
		return bills;
	}

	@Override
	public void update(Bill bill) {
		if (bill.getNumber()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+bill.getNumber());
		}
		billDAO.update(bill.getEntity());
	}

	@Override
	public int create(Bill bill) {
		int id = billDAO.create(bill.getEntity());
		return id;
	}

	@Override
	public Bill get(int number) {
		bill = new Bill();
		BillEntity billEntity = billDAO.get(number);
		if (billEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		bill.setEntity(billEntity);
		return bill;
	}

	@Override
	public void delete(int number) {
		bill = get(number);
		billDAO.delete(bill.getEntity());
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
	public int generateBill(Ride ride, RideRequest rideRequest){
		
		User passenger = rideRequest.getPassenger();
		User driver = ride.getDriver();
		float price = getFare(ride.getVehicle().getVehicleSubCategory(), driver);
		float distance = rideRequest.getTravelDistance();
		float amount = price * distance;
		Company company = RESTClientUtil.getCompany(1);
		float serviceChargePercentage = company.getServiceChargePercentage();
		//Set Bill properties
		bill.setAmount(amount);
		bill.setServiceChargePercentage(serviceChargePercentage);
		bill.setCompany(company);
		bill.setDriver(driver);
		bill.setPassenger(passenger);
		bill.setRide(ride);
		bill.setRideRequest(rideRequest);
		bill.setStatus(BillStatus.Pending);
		//Create Bill
		int number = create(bill);
		return number;
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
	
	private BillStatus getStatus(int billNumber){
		return billDAO.getStatus(billNumber);
	}
	
	public void approveBill(int billNumber){
		bill = get(billNumber);
		if (bill.getStatus().equals(BillStatus.Pending) || bill.getStatus().equals(BillStatus.Rejected)){
			bill.setStatus(BillStatus.Approved);
			update(bill);
		} else {
			throw new NotAcceptableException("Bill can't be approved as its not in valid state (Pending/Rejected). Bill current status:"+bill.getStatus());			
		}

	}
	
	public void rejectBill(int billNumber){
		bill = get(billNumber);
		if (bill.getStatus().equals(BillStatus.Pending)){
			bill.setStatus(BillStatus.Rejected);
			update(bill);
		} else {
			throw new NotAcceptableException("Bill can't be rejected as its not in valid state (Pending). Bill current status:"+bill.getStatus());			
		}
	}

	/*
	 * Purpose - Make payment to driver and company from Passenger account
	 * 
	 */
	public void makePayment(int billNumber, AccountType accountType){
		bill = get(billNumber);
		if (bill.getStatus().equals(BillStatus.Approved)){
			float amount = bill.getAmount();
			float serviceChargePercentage = bill.getServiceChargePercentage();
			float serviceCharge = amount * serviceChargePercentage;
			float driverAmount = amount - serviceCharge;
			//Get AccountDO for specific account type
			Account accountDO = getAccountDO(accountType);
			String remark = "Bill:"+billNumber+",Ride:"+bill.getRide().getId()+",RideRequest:"+bill.getRideRequest().getId();
			accountDO.debit(bill.getPassenger().getAccount(AccountType.Virtual).getNumber(), amount, remark);
			accountDO.credit(bill.getDriver().getAccount(AccountType.Virtual).getNumber(), driverAmount, remark);
			accountDO.debit(bill.getCompany().getAccount(AccountType.Virtual).getNumber(), serviceCharge, remark);		
			bill.setStatus(BillStatus.Paid);
			update(bill);
		} else {
			throw new NotAcceptableException("Can't make payment as bill is either not Approved or Paid. Bill current status:"+bill.getStatus());
		}
	}
	
	/*
	 * Purpose - It should return appropriate Account DO Impl based on Account type
	 */
	private Account getAccountDO(AccountType accountType){
		if (accountType.equals(AccountType.Virtual)){
			return new VirtualAccountDO();
		}
		throw new NotFoundException("No appropriate account DO found for account type:"+accountType);
	}

	@Override
	public Bill getWithEagerFetch(int number) {
		bill = get(number);
		bill.fetchReferenceVariable();
		return bill;
	}

}










































